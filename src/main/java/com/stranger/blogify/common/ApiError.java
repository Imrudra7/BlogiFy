package com.stranger.blogify.common;

import java.time.Instant;

public class ApiError {
    private Instant timestamp;
    private String message;
    private String path;

    public ApiError(String message, String path) {
        this.timestamp = Instant.now();
        this.message = message;
        this.path = path;
    }

    // getters

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
