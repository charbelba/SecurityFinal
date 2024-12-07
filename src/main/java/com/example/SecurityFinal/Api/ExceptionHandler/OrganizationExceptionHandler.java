package com.example.SecurityFinal.Api.ExceptionHandler;

import com.example.SecurityFinal.Api.Exception.ApiException;
import com.example.SecurityFinal.Api.Exception.InvitationAlreadyExistException;
import com.example.SecurityFinal.Api.Exception.OrganizationAlreadyExistException;
import com.example.SecurityFinal.Api.Exception.UserAlreadyInvitedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.management.relation.RoleNotFoundException;
import java.time.ZonedDateTime;
@ControllerAdvice
public class OrganizationExceptionHandler {
    @ExceptionHandler(OrganizationAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiException> handleOrganizationAlreadyExistException(OrganizationAlreadyExistException ex, WebRequest request) {
        ErrorResponse t;
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiException exception = ApiException.builder()
                .status(HttpStatus.CONFLICT)
                .message(ex.getMessage())
                .error(ex.getCause())
                .zonedDateTime(ZonedDateTime.now())
                .path(path)
                .build();
        return new ResponseEntity<>(exception, HttpStatus.CONFLICT);
    }



    @ExceptionHandler(InvitationAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiException> handleInvitationAlreadyExistException(InvitationAlreadyExistException ex, WebRequest request) {
        ErrorResponse t;
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiException exception = ApiException.builder()
                .status(HttpStatus.CONFLICT)
                .message(ex.getMessage())
                .error(ex.getCause())
                .zonedDateTime(ZonedDateTime.now())
                .path(path)
                .build();
        return new ResponseEntity<>(exception, HttpStatus.CONFLICT);
    }




    @ExceptionHandler(UserAlreadyInvitedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiException> handleInvitationAlreadyExistException(UserAlreadyInvitedException ex, WebRequest request) {
        ErrorResponse t;
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiException exception = ApiException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .error(ex.getCause())
                .zonedDateTime(ZonedDateTime.now())
                .path(path)
                .build();
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiException> handleRoleNotFoundException(RoleNotFoundException ex, WebRequest request) {
        ErrorResponse t;
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiException exception = ApiException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .error(ex.getCause())
                .zonedDateTime(ZonedDateTime.now())
                .path(path)
                .build();
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }




}