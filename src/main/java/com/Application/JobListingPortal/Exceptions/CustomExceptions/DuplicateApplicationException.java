package com.Application.JobListingPortal.Exceptions.CustomExceptions;

public class DuplicateApplicationException extends RuntimeException {
    public DuplicateApplicationException(String message) {
        super(message);
    }
}