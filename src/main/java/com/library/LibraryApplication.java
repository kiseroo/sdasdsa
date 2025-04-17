package com.library;

import com.library.controller.BookController;
import com.library.controller.BorrowController;
import com.library.controller.ReaderController;
import com.library.factory.BookFactory;
import com.library.factory.ReaderFactory;
import com.library.observer.BookAvailabilitySubject;
import com.library.observer.NotificationService;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRepository;
import com.library.repository.ReaderRepository;
import com.library.service.BookService;
import com.library.service.BorrowService;
import com.library.service.ReaderService;
import com.library.socket.NotificationServer;

import static spark.Spark.*;

public class LibraryApplication {
    public static void main(String[] args) {
        port(8080);
        
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With, Content-Length, Accept, Origin");
        });
        
        BookRepository bookRepository = new BookRepository();
        ReaderRepository readerRepository = new ReaderRepository();
        BorrowRepository borrowRepository = new BorrowRepository();
        
        BookFactory bookFactory = new BookFactory();
        ReaderFactory readerFactory = new ReaderFactory();
        
        BookAvailabilitySubject bookAvailabilitySubject = new BookAvailabilitySubject();
        
        NotificationServer notificationServer = new NotificationServer(9090);
        notificationServer.start();
        
        NotificationService notificationService = new NotificationService(
            bookRepository, readerRepository, notificationServer
        );
        
        BookService bookService = new BookService(bookRepository, bookFactory, bookAvailabilitySubject);
        ReaderService readerService = new ReaderService(readerRepository, readerFactory);
        BorrowService borrowService = new BorrowService(
            bookRepository, readerRepository, borrowRepository, notificationService
        );
        
        new BookController(bookService);
        new ReaderController(readerService);
        new BorrowController(borrowService);
        
        initializeTestData(bookService, readerService);
        
        Runtime.getRuntime().addShutdownHook(new Thread(notificationServer::stop));
    }
    
    private static void initializeTestData(BookService bookService, ReaderService readerService) {
        bookService.addBook("9780061120084", "To Kill a Mockingbird", "Harper Lee");
        bookService.addBook("9780141439518", "Pride and Prejudice", "Jane Austen");
        bookService.addBook("9780743273565", "The Great Gatsby", "F. Scott Fitzgerald");
        bookService.addBook("9780451524935", "1984", "George Orwell");
        bookService.addBook("9780618640157", "The Lord of the Rings", "J.R.R. Tolkien");
        
        readerService.registerReader("John Doe", "A12345");
        readerService.registerReader("Jane Smith", "B67890");
    }
} 