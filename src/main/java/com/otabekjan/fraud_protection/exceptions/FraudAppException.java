package com.otabekjan.fraud_protection.exceptions;

public class FraudAppException extends RuntimeException {
    public FraudAppException(String message) {
        super(message);
    }

    public FraudAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public FraudAppException(Throwable cause) {
        super(cause);
    }

    public FraudAppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FraudAppException() {
    }
}
