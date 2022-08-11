package com.example.airsofttechhelper.security;

import lombok.Getter;

@Getter
public class UnauthorisedAccessException extends RuntimeException{
    String message;
    public UnauthorisedAccessException(String message) {
        super(message);
        this.message = message;
    }
}
