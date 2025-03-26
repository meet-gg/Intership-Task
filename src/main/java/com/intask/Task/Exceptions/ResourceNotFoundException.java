package com.intask.Task.Exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(HttpStatus notFound, String message) {
        super(message);
    }
}
