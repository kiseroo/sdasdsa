package com.library.service;

import com.library.factory.BookFactory;
import com.library.model.Book;
import com.library.observer.BookAvailabilitySubject;
import com.library.repository.BookRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookServiceTest {
    private BookRepository bookRepository;
    private BookFactory bookFactory;
    private BookAvailabilitySubject bookAvailabilitySubject;
    private BookService bookService;

    @Before
    public void setUp() {
        bookRepository = new BookRepository();
        bookFactory = new BookFactory();
        bookAvailabilitySubject = new BookAvailabilitySubject();
        bookService = new BookService(bookRepository, bookFactory, bookAvailabilitySubject);
    }

    @Test
    public void testAddBook() {
        Book book = bookService.addBook("1234567890", "Test Book", "Test Author");
        
        assertNotNull(book);
        assertEquals("1234567890", book.getIsbn());
        assertEquals("Test Book", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertTrue(book.isAvailable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBookWithDuplicateIsbn() {
        bookService.addBook("1234567890", "Test Book", "Test Author");
        bookService.addBook("1234567890", "Another Book", "Another Author");
    }

    @Test
    public void testSearchBooksByTitle() {
        bookService.addBook("1234567890", "Java Programming", "Test Author");
        bookService.addBook("0987654321", "Python Basics", "Another Author");
        
        assertEquals(1, bookService.searchBooksByTitle("Java").size());
        assertEquals(0, bookService.searchBooksByTitle("C++").size());
    }
} 