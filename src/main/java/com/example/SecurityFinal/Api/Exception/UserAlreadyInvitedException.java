package com.example.SecurityFinal.Api.Exception;

public class UserAlreadyInvitedException extends RuntimeException{
    public UserAlreadyInvitedException(String s) {
        super(s,new Throwable(s));
    }

}
