package com.library.repository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class BorrowRepository {
    private final Map<String, Queue<String>> waitingLists = new HashMap<>();

    public void addToWaitingList(String isbn, String readerRegistrationNumber) {
        waitingLists.computeIfAbsent(isbn, k -> new LinkedList<>()).add(readerRegistrationNumber);
    }

    public String getNextReaderWaiting(String isbn) {
        Queue<String> queue = waitingLists.get(isbn);
        if (queue != null && !queue.isEmpty()) {
            return queue.poll();
        }
        return null;
    }

    public boolean isReaderWaiting(String isbn) {
        Queue<String> queue = waitingLists.get(isbn);
        return queue != null && !queue.isEmpty();
    }
} 