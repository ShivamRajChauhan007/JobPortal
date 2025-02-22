package com.Application.JobListingPortal.Exceptions.CustomExceptions;

public class InvalidJobTypeException extends RuntimeException {
    public InvalidJobTypeException(String message) {
        super(message);
    }
}
