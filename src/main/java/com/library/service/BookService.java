package com.library.service;

import com.library.factory.BookFactory;
import com.library.model.Book;
import com.library.observer.BookAvailabilitySubject;
import com.library.repository.BookRepository;
import java.util.List;

public class BookService {
    private final BookRepository bookRepository;
    private final BookFactory bookFactory;
    private final BookAvailabilitySubject bookAvailabilitySubject;

    public BookService(BookRepository bookRepository, BookFactory bookFactory, 
                       BookAvailabilitySubject bookAvailabilitySubject) {
        this.bookRepository = bookRepository;
        this.bookFactory = bookFactory;
        this.bookAvailabilitySubject = bookAvailabilitySubject;
    }

    public Book addBook(String isbn, String title, String author) {
        if (bookRepository.getBookByIsbn(isbn) != null) {
            throw new IllegalArgumentException("Book with ISBN " + isbn + " already exists");
        }
        
        Book book = bookFactory.createBook(isbn, title, author);
        bookRepository.addBook(book);
        return book;
    }

    public Book getBookByIsbn(String isbn) {
        Book book = bookRepository.getBookByIsbn(isbn);
        if (book == null) {
            throw new IllegalArgumentException("Book with ISBN " + isbn + " not found");
        }
        return book;
    }

    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.getAvailableBooks();
    }

    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.searchBooksByTitle(title);
    }

    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.searchBooksByAuthor(author);
    }

    public void markBookAsUnavailable(String isbn) {
        Book book = getBookByIsbn(isbn);
        book.setAvailable(false);
        bookRepository.updateBook(book);
    }

    public void markBookAsAvailable(String isbn) {
        Book book = getBookByIsbn(isbn);
        book.setAvailable(true);
        bookRepository.updateBook(book);
        
        bookAvailabilitySubject.notifyObservers(isbn);
    }
} 