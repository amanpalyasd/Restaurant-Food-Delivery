package com.util.rfd.CustomException;

public class EmailCannotBeUpdated extends RuntimeException{

    public EmailCannotBeUpdated(String message) {
        super(message);
    }
}
