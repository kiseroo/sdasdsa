package com.library.repository;

import com.library.model.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReaderRepository {
    private final Map<String, Reader> readers = new HashMap<>();

    public void addReader(Reader reader) {
        readers.put(reader.getRegistrationNumber(), reader);
    }

    public Reader getReaderByRegistrationNumber(String registrationNumber) {
        return readers.get(registrationNumber);
    }

    public List<Reader> getAllReaders() {
        return new ArrayList<>(readers.values());
    }

    public boolean updateReader(Reader reader) {
        if (readers.containsKey(reader.getRegistrationNumber())) {
            readers.put(reader.getRegistrationNumber(), reader);
            return true;
        }
        return false;
    }
} 