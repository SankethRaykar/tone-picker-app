package com.tonepicker.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

// @Data is a Lombok annotation that generates getters, setters, toString(), etc.
// This reduces boilerplate code.
@Data
// @AllArgsConstructor generates a constructor with one parameter for each field in the class.
// This is useful for creating new instances of this model.
@AllArgsConstructor
public class ToneResponse {
    // This field holds the text that has been rewritten by the AI.
    // It is the main data returned to the frontend.
    private String adjustedText;
}