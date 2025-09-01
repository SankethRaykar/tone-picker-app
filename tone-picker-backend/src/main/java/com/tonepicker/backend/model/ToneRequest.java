package com.tonepicker.backend.model;

// @NotBlank is a Jakarta Validation annotation to ensure the field is not null or empty.
import jakarta.validation.constraints.NotBlank;
// @Data is a Lombok annotation that automatically generates boilerplate code
// like getters, setters, toString(), equals(), and hashCode() methods.
import lombok.Data;

// This class represents the request body coming from the frontend.
// It holds the data needed to make a tone adjustment request.
@Data
public class ToneRequest {
    // The text to be rewritten. @NotBlank ensures this field is not empty.
    @NotBlank(message = "Text cannot be empty")
    private String text;
    
    // The x-coordinate from the tone grid, representing the horizontal tone (e.g., concise, expanded).
    private int x; 
    
    // The y-coordinate from the tone grid, representing the vertical tone (e.g., formal, casual).
    private int y; 
}