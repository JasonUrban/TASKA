package com.example.taska.exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(Long id) {
        super("Could not find account " + id);
    }
}
