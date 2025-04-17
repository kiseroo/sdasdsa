package com.library.service;

import com.library.factory.ReaderFactory;
import com.library.model.Reader;
import com.library.repository.ReaderRepository;
import java.util.List;

public class ReaderService {
    private final ReaderRepository readerRepository;
    private final ReaderFactory readerFactory;

    public ReaderService(ReaderRepository readerRepository, ReaderFactory readerFactory) {
        this.readerRepository = readerRepository;
        this.readerFactory = readerFactory;
    }

    public Reader registerReader(String name, String registrationNumber) {
        if (readerRepository.getReaderByRegistrationNumber(registrationNumber) != null) {
            throw new IllegalArgumentException("Reader with registration number " + 
                                               registrationNumber + " already exists");
        }
        
        Reader reader = readerFactory.createReader(name, registrationNumber);
        readerRepository.addReader(reader);
        return reader;
    }

    public Reader getReaderByRegistrationNumber(String registrationNumber) {
        Reader reader = readerRepository.getReaderByRegistrationNumber(registrationNumber);
        if (reader == null) {
            throw new IllegalArgumentException("Reader with registration number " + 
                                               registrationNumber + " not found");
        }
        return reader;
    }

    public List<Reader> getAllReaders() {
        return readerRepository.getAllReaders();
    }

    public boolean updateReader(Reader reader) {
        return readerRepository.updateReader(reader);
    }
} 