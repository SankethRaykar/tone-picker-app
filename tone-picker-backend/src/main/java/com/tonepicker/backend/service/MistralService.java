package com.tonepicker.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// @Service marks this class as a Spring service component.
// It is a key part of the service layer in the application's architecture.
@Service
public class MistralService {

    // WebClient is a non-blocking, reactive HTTP client used for making API calls.
    private final WebClient webClient;
    // ObjectMapper from Jackson is used for converting JSON to Java objects and vice-versa.
    private final ObjectMapper mapper = new ObjectMapper();

    // @Value injects a value from the application's properties file (application.properties).
    // This securely injects the API key, preventing it from being hardcoded.
    @Value("${mistral.api.key}")
    private String apiKey; 

    // A Java record for a simple, immutable data holder.
    // It's used to store cached values along with their expiration timestamp.
    private record CacheEntry(String value, long expiresAt) {}
    // A thread-safe map to store cached responses. This is a simple in-memory cache.
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    // Time-to-live (TTL) for cache entries, set to 10 minutes.
    private final long TTL_MS = 10 * 60 * 1000; 

    // The constructor initializes the WebClient with the base URL for the Mistral API.
    public MistralService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.mistral.ai/v1")
                .build();
    }

    // This method generates a unique key for caching based on the input text and tone coordinates.
    // It uses the text's hash code to create a compact key.
    private String key(String text, int x, int y) {
        int h = (text == null ? 0 : text.hashCode());
        return x + ":" + y + ":" + h;
    }

    // This method maps the x and y coordinates from the frontend to a specific tone instruction for the AI.
    private String instructionFor(int x, int y){
        String[] formality = {"very formal","formal","casual"};
        String[] warmth = {"stern","neutral","warm"};
        // Use Math.max/min to ensure array indices are always within bounds [0, 2].
        String f = formality[Math.max(0, Math.min(2, x))];
        String w = warmth[Math.max(0, Math.min(2, y))];

        return String.format(
            "Rewrite the user's text with tone: %s and %s. Preserve meaning. " +
            "Do not add explanations, questions, or extra commentary. Return only the rewritten text.", f, w
        );
    }

    // The main public method for rewriting text using the Mistral AI API.
    public Mono<String> rewrite(String text, int x, int y){
        // Basic validation: if text is empty, return an empty Mono.
        if(text == null || text.trim().isEmpty()) return Mono.empty();

        String k = key(text, x, y);
        CacheEntry cached = cache.get(k);
        long now = Instant.now().toEpochMilli();
        // Check if a valid, non-expired entry exists in the cache. If so, return it immediately.
        if(cached != null && cached.expiresAt > now) return Mono.just(cached.value);

        // Security check: ensure the API key is set before proceeding.
        if(apiKey == null || apiKey.isBlank()) 
            return Mono.error(new RuntimeException("MISTRAL_API_KEY not set"));

        // Define the system and user instructions for the AI chat model.
        String system = "You are a concise assistant that rewrites text only to match requested tone.";
        String instruction = instructionFor(x, y);

        // Build the request body as a Map, which Jackson will convert to JSON.
        Map<String, Object> body = Map.of(
            "model", "mistral-small",
            "temperature", 0.3,
            "messages", new Object[]{
                Map.of("role","system","content",system),
                Map.of("role","user","content",instruction + "\n\n---\nUser text:\n" + text)
            }
        );

        // Start the reactive HTTP request chain using WebClient.
        return webClient.post()
                // Set the specific API endpoint.
                .uri("/chat/completions")
                // Add the Authorization header with the Bearer token (API key).
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + apiKey)
                // Set the content type to JSON.
                .contentType(MediaType.APPLICATION_JSON)
                // Provide the request body.
                .bodyValue(body)
                // Send the request and retrieve the response.
                .retrieve()
                // Extract the response body as a String.
                .bodyToMono(String.class)
                // .map() to process the string response.
                .map(resp -> {
                    try{
                        // Parse the JSON string into a tree structure.
                        JsonNode root = mapper.readTree(resp);
                        // Navigate the JSON to find the AI's message.
                        JsonNode choice = root.path("choices").isArray() ? root.path("choices").get(0) : null;
                        String out = "";
                        if(choice != null && choice.has("message")) {
                            out = choice.path("message").path("content").asText("");
                        }
                        // Cache the successful response with a new expiration time.
                        cache.put(k, new CacheEntry(out, now + TTL_MS));
                        return out;
                    } catch(Exception e){
                        // If JSON parsing fails, throw a runtime exception.
                        throw new RuntimeException("Parsing upstream response failed: " + e.getMessage(), e);
                    }
                });
    }
}