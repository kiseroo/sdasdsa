package com.library.observer;

import com.library.model.Book;
import com.library.model.Reader;
import com.library.repository.BookRepository;
import com.library.repository.ReaderRepository;
import com.library.socket.NotificationServer;

public class NotificationService implements BookAvailabilityObserver {
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final NotificationServer notificationServer;

    public NotificationService(BookRepository bookRepository, ReaderRepository readerRepository, 
                             NotificationServer notificationServer) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.notificationServer = notificationServer;
    }

    @Override
    public void update(String isbn) {
        Book book = bookRepository.getBookByIsbn(isbn);
        if (book != null && book.isAvailable()) {
            String message = "Book with ISBN " + isbn + " (" + book.getTitle() + ") is now available.";
            notificationServer.broadcastMessage(message);
        }
    }

    public void notifyReader(String readerRegistrationNumber, String message) {
        Reader reader = readerRepository.getReaderByRegistrationNumber(readerRegistrationNumber);
        if (reader != null) {
            notificationServer.sendMessage(readerRegistrationNumber, message);
        }
    }
} 