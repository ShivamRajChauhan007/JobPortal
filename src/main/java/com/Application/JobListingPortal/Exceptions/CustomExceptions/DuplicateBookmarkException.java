package com.Application.JobListingPortal.Exceptions.CustomExceptions;


public class DuplicateBookmarkException extends RuntimeException {
    public DuplicateBookmarkException(String message) {
        super(message);
    }
}
