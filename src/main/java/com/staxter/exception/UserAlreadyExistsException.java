package com.staxter.exception;

/**
 * Exception thrown when an existing user exists in the local storage with the provided username
 *
 * @author Julian Vasa
 */
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(){
        super("User already exists");
    }
}
