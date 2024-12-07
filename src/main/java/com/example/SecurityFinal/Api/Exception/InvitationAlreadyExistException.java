package com.example.SecurityFinal.Api.Exception;

public class InvitationAlreadyExistException extends RuntimeException{
    public InvitationAlreadyExistException(String s) {
        super(s,new Throwable(s));
    }

}
