package com.tonepicker.backend.controller;

import com.tonepicker.backend.model.ToneRequest;
import com.tonepicker.backend.model.ToneResponse;
import com.tonepicker.backend.service.MistralService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

// @RestController combines @Controller and @ResponseBody, making it a RESTful controller.
// It handles incoming HTTP requests and sends back responses directly as data (like JSON).
@RestController

// @RequestMapping sets the base path for all endpoints in this controller to "/api/tone".
@RequestMapping("/api/tone")

// @CrossOrigin enables Cross-Origin Resource Sharing for all methods in this controller.
// It allows the frontend (running on a different origin) to make requests to this backend.
@CrossOrigin(origins = "*")
public class ToneController {

    // This is a dependency injection. Spring automatically provides an instance of MistralService.
    private final MistralService mistralService;

    // The constructor handles the dependency injection for MistralService.
    public ToneController(MistralService mistralService) {
        this.mistralService = mistralService;
    }

    // @PostMapping maps HTTP POST requests to the "/adjust" endpoint.
    @PostMapping("/adjust")

    // @RequestBody automatically converts the incoming JSON request body into a ToneRequest object.
    // Mono<ResponseEntity<Object>> indicates an asynchronous operation (using Reactive Streams)
    // that will eventually return an HTTP response entity.
    public Mono<ResponseEntity<Object>> adjustTone(@RequestBody ToneRequest request) {

        // This is a basic validation check to handle an empty text field.
        // It's an important edge case to handle gracefully.
        if(request.getText() == null || request.getText().trim().isEmpty()){
            // If the text is empty, return a 400 Bad Request status with an error message.
            return Mono.just(ResponseEntity.badRequest()
                    .body(Map.of("error", "EMPTY_TEXT")));
        }

        // Call the service layer to perform the tone rewriting.
        // The service method returns a Mono<String> (an asynchronous result).
        return mistralService.rewrite(request.getText(), request.getX(), request.getY())
                // .map() is used to transform the successful result (the adjusted text string)
                // into a successful HTTP response with a 200 OK status.
                .map(adjustedText -> ResponseEntity.ok((Object) new ToneResponse(adjustedText)))
                // .onErrorResume() is a reactive error handling operator.
                // If any error occurs during the 'rewrite' call, this block is executed.
                .onErrorResume(e -> Mono.just(ResponseEntity.status(502)
                        // Return a 502 Bad Gateway status to indicate an upstream service error (from Mistral AI).
                        .body((Object) Map.of("error", "UPSTREAM_ERROR", "message", e.getMessage()))));
    }
}