package com.stranger.blogify.common;

public class ApiResponse {
    private String message; // field private kar do for good practice
    private Object object;
    private Boolean success;
    public ApiResponse() {
        // No-arg constructor required for JSON serialization/deserialization
    }
    public ApiResponse(String apiResponse) {
        this.message = apiResponse;
        this.object = null;
        this.success=null;
    }
    public ApiResponse(String apiResponse,Object object, Boolean success) {
        this.message = apiResponse;
        this.object = object;
        this.success= success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String apiResponse) {
        this.message = apiResponse;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
