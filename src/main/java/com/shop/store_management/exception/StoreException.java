package com.shop.store_management.exception;


public sealed class StoreException extends RuntimeException
        permits ResourceNotFoundException, ValidationException, UnauthorizedAccessException {

    public StoreException(String message) {
        super(message);
    }
}