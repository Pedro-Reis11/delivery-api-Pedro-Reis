package com.delivery_api.Projeto.Delivery.API.exception;

public class ConflictException extends BusinessException  {
    private String conflictField;
    private Object conflictValue;

    public ConflictException(String message) {
        super(message);
        this.setErrorCode("CONFLICT");
    }

    public ConflictException(String message, String conflictField, Object conflictValue) {
        super(message);
        this.conflictField = conflictField;
        this.conflictValue = conflictValue;
        this.setErrorCode("CONFLICT");
    }

    public String getConflictField() {
        return conflictField;
    }

    public Object getConflictValue() {
        return conflictValue;
    }
}
