package com.miskowiec.airsofttechhelper.security;

public class UnauthorisedAccessException extends RuntimeException{
    public UnauthorisedAccessException(String message) {
        super(message);
    }
}
