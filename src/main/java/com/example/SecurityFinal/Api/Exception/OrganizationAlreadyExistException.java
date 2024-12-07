package com.example.SecurityFinal.Api.Exception;

public class OrganizationAlreadyExistException extends RuntimeException{
    public OrganizationAlreadyExistException(String s) {
        super(s,new Throwable(s));
    }
}
