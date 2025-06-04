package com.util.rfd.CustomException;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class EmailAlreadyRegisteredException extends RuntimeException{
    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
