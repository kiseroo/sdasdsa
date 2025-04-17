package com.library.service;

import com.library.model.Book;
import com.library.model.Reader;
import com.library.observer.NotificationService;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRepository;
import com.library.repository.ReaderRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BorrowService {
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final BorrowRepository borrowRepository;
    private final NotificationService notificationService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public BorrowService(BookRepository bookRepository, ReaderRepository readerRepository,
                         BorrowRepository borrowRepository, NotificationService notificationService) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.borrowRepository = borrowRepository;
        this.notificationService = notificationService;
    }

    public boolean borrowBook(String readerRegistrationNumber, String isbn) {
        Reader reader = readerRepository.getReaderByRegistrationNumber(readerRegistrationNumber);
        Book book = bookRepository.getBookByIsbn(isbn);
        
        if (reader == null) {
            throw new IllegalArgumentException("Reader not found");
        }
        
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        
        if (reader.getBorrowedBooks().size() >= 5) {
            return false;
        }
        
        if (!book.isAvailable()) {
            borrowRepository.addToWaitingList(isbn, readerRegistrationNumber);
            return false;
        }
        
        String borrowDate = LocalDate.now().format(formatter);
        boolean borrowed = reader.borrowBook(book, borrowDate);
        
        if (borrowed) {
            book.setAvailable(false);
            bookRepository.updateBook(book);
            readerRepository.updateReader(reader);
        }
        
        return borrowed;
    }

    public boolean returnBook(String readerRegistrationNumber, String isbn) {
        Reader reader = readerRepository.getReaderByRegistrationNumber(readerRegistrationNumber);
        Book book = bookRepository.getBookByIsbn(isbn);
        
        if (reader == null) {
            throw new IllegalArgumentException("Reader not found");
        }
        
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        
        boolean returned = reader.returnBook(book);
        
        if (returned) {
            String nextReaderRegistrationNumber = borrowRepository.getNextReaderWaiting(isbn);
            
            if (nextReaderRegistrationNumber != null) {
                Reader nextReader = readerRepository.getReaderByRegistrationNumber(nextReaderRegistrationNumber);
                if (nextReader != null) {
                    String message = "The book you requested (ISBN: " + isbn + " - " + 
                                    book.getTitle() + ") is now available.";
                    notificationService.notifyReader(nextReaderRegistrationNumber, message);
                    
                    borrowBook(nextReaderRegistrationNumber, isbn);
                }
            } else {
                book.setAvailable(true);
                bookRepository.updateBook(book);
            }
            
            readerRepository.updateReader(reader);
        }
        
        return returned;
    }

    public void reserveBook(String readerRegistrationNumber, String isbn) {
        Reader reader = readerRepository.getReaderByRegistrationNumber(readerRegistrationNumber);
        Book book = bookRepository.getBookByIsbn(isbn);
        
        if (reader == null) {
            throw new IllegalArgumentException("Reader not found");
        }
        
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        
        borrowRepository.addToWaitingList(isbn, readerRegistrationNumber);
    }
} 