package com.buevich.labs.database.errors;

public class LabotoryRuntimeException extends RuntimeException {

    public LabotoryRuntimeException(String message) {
        super(message);
    }

    public LabotoryRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}