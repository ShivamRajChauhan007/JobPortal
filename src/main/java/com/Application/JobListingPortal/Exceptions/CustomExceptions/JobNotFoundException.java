package com.Application.JobListingPortal.Exceptions.CustomExceptions;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(String message) {
        super(message);
    }
}
