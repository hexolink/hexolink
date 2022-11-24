package com.auth.util.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends ApplicationException {
    public static final String AUTHENTICATION_EXCEPTION = "exception.common.notAuthenticated";

    public AuthenticationException() {
        super(ErrorType.AUTHENTICATION_EXCEPTION, AUTHENTICATION_EXCEPTION, HttpStatus.UNAUTHORIZED);
    }
}
