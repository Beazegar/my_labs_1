package com.buevich.labs.database.errors;

public class Exceptions extends Exception {

    public Exceptions(String message) {
        super(message);
    }

    public Exceptions(String message, Throwable cause) {
        super(message, cause);
    }
}