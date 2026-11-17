
package com.smartsugar.backend.dto;

public class HealthResponse {

    private String application;
    private String status;
    private String message;

    public HealthResponse(String application, String status, String message) {
        this.application = application;
        this.status = status;
        this.message = message;
    }

    public String getApplication() {
        return application;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
