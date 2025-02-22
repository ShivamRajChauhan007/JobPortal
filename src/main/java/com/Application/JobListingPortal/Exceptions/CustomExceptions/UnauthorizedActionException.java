package com.Application.JobListingPortal.Exceptions.CustomExceptions;

public class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
