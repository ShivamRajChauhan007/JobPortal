package com.Application.JobListingPortal.Exceptions.CustomExceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String messaage){
        super(messaage);
    }
}
