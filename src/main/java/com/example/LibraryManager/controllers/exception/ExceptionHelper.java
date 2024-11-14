package com.example.LibraryManager.controllers.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ExceptionHelper {

    // Handle constraint violations for request parameters or path variables or entity constraints
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));
        return responseEntity(errors, HttpStatus.BAD_REQUEST, ex);
    }

    // Handle @Valid and @Validated exceptions for request body validations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return responseEntity(errors, HttpStatus.BAD_REQUEST, ex);
    }

    // db data integrity exceptions, e.g unique column violation
    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<Map<String, String>> handleIntegrityException(DataIntegrityViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        addDataIntegrityDetails(errors, ex);
        return responseEntity(errors, HttpStatus.CONFLICT, ex);
    }

    // not found exception
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(NoSuchElementException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());

        return responseEntity(errors, HttpStatus.NOT_FOUND, ex);
    }

    // Generic exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());

        return responseEntity(errors, HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private ResponseEntity<Map<String, String>> responseEntity(
            Map<String, String> errors, HttpStatus httpStatus, Exception ex) {
        errors.put("timestamp", String.valueOf(LocalDateTime.now()));
        errors.put("status", String.valueOf(httpStatus.value()));

        if(!errors.containsKey("error")) {
            errors.put("error", "An error occurred. Please try again later. ");
        }

        log.error("{} : {}", ex.getClass().getName(), errors);
        return new ResponseEntity<>(errors, httpStatus);
    }

    private void addDataIntegrityDetails(Map<String, String> errors, DataIntegrityViolationException ex) {
        // Extract the cause and customize the message
        String error = "An error occurred. Ensure the data is correct. ";
        String details = "";
        if (ex.getRootCause() != null) {
            String originalCauseMessage = ex.getRootCause().getMessage();
            String causeMessage = originalCauseMessage.toLowerCase();

            // Check for constraint violation messages to give a specific user details
            if (causeMessage.contains("constraint")) {
                if (causeMessage.contains("unique")) {
                    error = "A unique constraint violation occurred. Please ensure the data is unique.";
                } else if (causeMessage.contains("foreign key")) {
                    error = "A foreign key constraint violation occurred. Ensure the related data exists.";
                } else if (causeMessage.contains("not null")) {
                    error = "A not-null constraint violation occurred. Ensure all required fields are filled.";
                }
            }
            errors.put("error", error);

            //optional more details
            String detailTitle = "detail:";
            int detailIndex =  causeMessage.indexOf(detailTitle);
            if(detailIndex > -1) {
                details = originalCauseMessage.substring(detailIndex + detailTitle.length());
                errors.put("details", details);
            }
        }
    }

}
