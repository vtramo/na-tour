package com.natour.natour.exceptions;

public class EntityByIdNotFoundException extends RuntimeException {
    public EntityByIdNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
