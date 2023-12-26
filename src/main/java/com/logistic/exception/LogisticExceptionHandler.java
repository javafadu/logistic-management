package com.logistic.exception;


import com.logistic.exception.messages.ApiResponseError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice // Central Exception Handling
// ResponseEntityExceptionHandler: Spring Boot Exception Handler
public class LogisticExceptionHandler extends ResponseEntityExceptionHandler {
    // AIM: set up a custom exception system
    // override throwable exceptions and response with our APIResponse structure


    // Logger:
    Logger logger = LoggerFactory.getLogger(LogisticExceptionHandler.class);


    // method for response entity
    private ResponseEntity<Object> buildResponseEntity(ApiResponseError error) {
        // logging every handled exceptions so added into to the buildResponseEntity method
        logger.error(error.getMessage());
        return new ResponseEntity<>(error, error.getStatus());
    }

    // child class for exception - 1
    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        ApiResponseError error = new ApiResponseError(
                HttpStatus.NOT_FOUND, // status
                ex.getMessage(), // message
                request.getDescription(false)); // hide unnecessary information

        return buildResponseEntity(error);

    }


    // child class for exception - 2
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.toList());

        ApiResponseError error = new ApiResponseError(
                HttpStatus.BAD_REQUEST,
                errors.get(0).toString(),
                request.getDescription(false));

        return buildResponseEntity(error);

    }

    // child class for exception - 3
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false));

        return buildResponseEntity(error);
    }

    // child class for exception - 4
    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false));

        return buildResponseEntity(error);
    }

    // child class for exception - 5
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false));

        return buildResponseEntity(error);
    }



    // Father Class for exception
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleRunTimeException(RuntimeException ex, WebRequest request) {
        ApiResponseError error = new ApiResponseError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false) );

        return buildResponseEntity(error);

    }


    // Grand Father Class for exception
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {

        ApiResponseError error = new ApiResponseError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false));

        return buildResponseEntity(error);


    }


}
