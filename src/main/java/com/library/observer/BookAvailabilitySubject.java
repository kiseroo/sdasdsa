package com.library.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookAvailabilitySubject {
    private final Map<String, List<BookAvailabilityObserver>> observers = new HashMap<>();

    public void addObserver(String isbn, BookAvailabilityObserver observer) {
        observers.computeIfAbsent(isbn, k -> new ArrayList<>()).add(observer);
    }

    public void removeObserver(String isbn, BookAvailabilityObserver observer) {
        if (observers.containsKey(isbn)) {
            observers.get(isbn).remove(observer);
        }
    }

    public void notifyObservers(String isbn) {
        if (observers.containsKey(isbn)) {
            for (BookAvailabilityObserver observer : observers.get(isbn)) {
                observer.update(isbn);
            }
        }
    }
} 