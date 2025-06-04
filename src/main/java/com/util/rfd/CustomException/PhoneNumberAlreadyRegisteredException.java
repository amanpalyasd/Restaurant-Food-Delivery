package com.util.rfd.CustomException;

public class PhoneNumberAlreadyRegisteredException extends RuntimeException{
    public PhoneNumberAlreadyRegisteredException(String message) {
        super(message);
    }
}
