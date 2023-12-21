package com.logistic.exception.messages;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiResponseError {
    // AIM: Main Template for Customized error messages

    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeStamp;

    private String message;

    private String requestURI;


    // Constructors (Not use no args constructor so we  will set it as private)
    private ApiResponseError() {
        timeStamp = LocalDateTime.now();
    }

    public ApiResponseError(HttpStatus status) {
        this(); // call above no arg constructor
        this.message="Unexpected Errors";
        this.status=status;
    }

    public ApiResponseError(HttpStatus status, String message, String requestURI) {
        this(status); // call above 1 arg constructor
        this.message=message;
        this.requestURI=requestURI;
    }


    // GETTER & SETTER


    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }
}
