package com.library.factory;

import com.library.model.Reader;

public class ReaderFactory {
    public Reader createReader(String name, String registrationNumber) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be empty");
        }
        
        return new Reader(name, registrationNumber);
    }
} 