package com.Application.JobListingPortal.Exceptions.CustomExceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

