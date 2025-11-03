package com.shipping.booking.exception;

public class AvailabilityCheckException extends RuntimeException {
    public AvailabilityCheckException(String message) {
        super(message);
    }
    
    public AvailabilityCheckException(String message, Throwable cause) {
        super(message, cause);
    }
}