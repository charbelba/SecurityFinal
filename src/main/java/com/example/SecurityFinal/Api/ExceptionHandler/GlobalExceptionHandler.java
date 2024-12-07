package com.example.SecurityFinal.Api.ExceptionHandler;



import com.example.SecurityFinal.Api.Exception.ApiException;
import com.example.SecurityFinal.Api.Exception.ApiRequestException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.annotation.Order;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.ZonedDateTime;
import java.util.List;

@Order(5)
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<ApiException> handleApiRequestException(ApiRequestException ex, WebRequest request) {
        ErrorResponse t;
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiException exception = ApiException.builder()
                .status(ex.getStatus())
                .message(ex.getMessage())
                .error(ex.getCause())// temporary
                .zonedDateTime(ZonedDateTime.now())
                .path(path)
                .build();
        return new ResponseEntity<>(exception,ex.getStatus());
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiException> handleNoHandlerException(NoHandlerFoundException ex, WebRequest request) {
        ErrorResponse t;
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiException exception = ApiException.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("endpoint not found")
                .error(ex.getCause())
                .zonedDateTime(ZonedDateTime.now())
                .path(path)
                .build();
        return new ResponseEntity<>(exception,HttpStatus.NOT_FOUND);
    }




    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiException> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        ErrorResponse t;

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiException exception = ApiException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .error(ex.getCause())
                .zonedDateTime(ZonedDateTime.now())
                .path(path)
                .build();
        return new ResponseEntity<>(exception,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NotImplementedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiException> handleNotImplementedException(NotImplementedException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiException exception = ApiException.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ex.getMessage())
                .error(ex.getCause())
                .zonedDateTime(ZonedDateTime.now())
                .path(path)
                .build();
        return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiException> handleValidationExceptions(MethodArgumentNotValidException ex,WebRequest request) {
        List<String> errorList = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .toList();
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        ApiException exception = ApiException.builder()
                .message(errorList.toString())
                .error(ex.getCause())
                .status(HttpStatus.BAD_REQUEST)
                .zonedDateTime(ZonedDateTime.now())
                .path(path)
                .build();
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiException> handlePropertyReferenceException(PropertyReferenceException ex,WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiException exception = ApiException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .error(ex.getCause())
                .zonedDateTime(ZonedDateTime.now())
                .path(path)
                .build();
        return new ResponseEntity<>(exception,HttpStatus.BAD_REQUEST);
    }


/*
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiException> handleApiRequestException(RuntimeException ex, WebRequest request) {
        ErrorResponse t;
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiException exception = ApiException.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ex.getMessage())
                .error(ex.getCause())
                .zonedDateTime(ZonedDateTime.now())
                .path(path)
                .build();
        return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
    }
*/


}