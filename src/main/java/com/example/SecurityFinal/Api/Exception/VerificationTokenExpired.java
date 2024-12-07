package com.example.SecurityFinal.Api.Exception;

public class VerificationTokenExpired extends RuntimeException{
    public VerificationTokenExpired(String s) {
        super(s,new Throwable(s));
    }

}
