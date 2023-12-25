package com.logistic.exception;


import com.logistic.exception.messages.ApiResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice // Central Exception Handling
// ResponseEntityExceptionHandler: Spring Boot Exception Handler
public class LogisticExceptionHandler extends ResponseEntityExceptionHandler {
    // AIM: set up a custom exception system
    // override throwable exceptions and response with our APIResponse structure

    // method for response entity
    private ResponseEntity<Object> buildResponseEntity(ApiResponseError error) {
        return new ResponseEntity<>(error, error.getStatus());
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        ApiResponseError error = new ApiResponseError(
                HttpStatus.NOT_FOUND, // status
                ex.getMessage(), // message
                request.getDescription(false)); // hide unnecessary information

        return buildResponseEntity(error);

    }

}
