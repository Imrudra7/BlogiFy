package com.stranger.blogify.common;

public class ApiResponse {
    private String message; // field private kar do for good practice

    public ApiResponse() {
        // No-arg constructor required for JSON serialization/deserialization
    }

    public ApiResponse(String apiResponse) {
        this.message = apiResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String apiResponse) {
        this.message = apiResponse;
    }
}
