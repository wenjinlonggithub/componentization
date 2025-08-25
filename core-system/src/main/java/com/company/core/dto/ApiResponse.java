package com.company.core.dto;

/**
 * Generic API Response wrapper
 * Used for consistent REST API responses
 */
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private String errorCode;

    public ApiResponse() {}

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Static factory methods
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message);
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        ApiResponse<T> response = new ApiResponse<>(false, message);
        response.setErrorCode(errorCode);
        return response;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
}