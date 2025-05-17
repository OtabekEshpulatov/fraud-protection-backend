package com.otabekjan.fraud_protection.exceptions;

public class DeveloperException extends FraudAppException{
    public DeveloperException(String message) {
        super(message);
    }

    public DeveloperException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeveloperException(Throwable cause) {
        super(cause);
    }

    public DeveloperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DeveloperException() {
    }
}
