package com.library.controller;

import com.google.gson.Gson;
import com.library.model.Book;
import com.library.service.BookService;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class BookController {
    private final BookService bookService;
    private final Gson gson = new Gson();

    public BookController(BookService bookService) {
        this.bookService = bookService;
        setupRoutes();
    }

    private void setupRoutes() {
        get("/api/books", this::getAllBooks);
        get("/api/books/:isbn", this::getBookByIsbn);
        get("/api/books/available", this::getAvailableBooks);
        get("/api/books/search/title/:title", this::searchBooksByTitle);
        get("/api/books/search/author/:author", this::searchBooksByAuthor);
        post("/api/books", this::addBook);
    }

    private Object getAllBooks(Request request, Response response) {
        response.type("application/json");
        return gson.toJson(bookService.getAllBooks());
    }

    private Object getBookByIsbn(Request request, Response response) {
        response.type("application/json");
        String isbn = request.params(":isbn");
        try {
            Book book = bookService.getBookByIsbn(isbn);
            return gson.toJson(book);
        } catch (IllegalArgumentException e) {
            response.status(404);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private Object getAvailableBooks(Request request, Response response) {
        response.type("application/json");
        return gson.toJson(bookService.getAvailableBooks());
    }

    private Object searchBooksByTitle(Request request, Response response) {
        response.type("application/json");
        String title = request.params(":title");
        return gson.toJson(bookService.searchBooksByTitle(title));
    }

    private Object searchBooksByAuthor(Request request, Response response) {
        response.type("application/json");
        String author = request.params(":author");
        return gson.toJson(bookService.searchBooksByAuthor(author));
    }

    private Object addBook(Request request, Response response) {
        response.type("application/json");
        try {
            BookRequest bookRequest = gson.fromJson(request.body(), BookRequest.class);
            Book book = bookService.addBook(
                bookRequest.isbn,
                bookRequest.title,
                bookRequest.author
            );
            response.status(201);
            return gson.toJson(book);
        } catch (IllegalArgumentException e) {
            response.status(400);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private static class BookRequest {
        private String isbn;
        private String title;
        private String author;
    }

    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }
} 