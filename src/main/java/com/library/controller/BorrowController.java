package com.library.controller;

import com.google.gson.Gson;
import com.library.service.BorrowService;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class BorrowController {
    private final BorrowService borrowService;
    private final Gson gson = new Gson();

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
        setupRoutes();
    }

    private void setupRoutes() {
        post("/api/borrow", this::borrowBook);
        post("/api/return", this::returnBook);
        post("/api/reserve", this::reserveBook);
    }

    private Object borrowBook(Request request, Response response) {
        response.type("application/json");
        try {
            BorrowRequest borrowRequest = gson.fromJson(request.body(), BorrowRequest.class);
            boolean success = borrowService.borrowBook(
                borrowRequest.readerRegistrationNumber,
                borrowRequest.isbn
            );
            
            if (success) {
                return gson.toJson(new SuccessResponse("Book borrowed successfully"));
            } else {
                response.status(400);
                return gson.toJson(new ErrorResponse("Could not borrow book. It might be unavailable or you've reached the maximum limit."));
            }
        } catch (IllegalArgumentException e) {
            response.status(400);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private Object returnBook(Request request, Response response) {
        response.type("application/json");
        try {
            BorrowRequest returnRequest = gson.fromJson(request.body(), BorrowRequest.class);
            boolean success = borrowService.returnBook(
                returnRequest.readerRegistrationNumber,
                returnRequest.isbn
            );
            
            if (success) {
                return gson.toJson(new SuccessResponse("Book returned successfully"));
            } else {
                response.status(400);
                return gson.toJson(new ErrorResponse("Could not return book. It might not be borrowed by this reader."));
            }
        } catch (IllegalArgumentException e) {
            response.status(400);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private Object reserveBook(Request request, Response response) {
        response.type("application/json");
        try {
            BorrowRequest reserveRequest = gson.fromJson(request.body(), BorrowRequest.class);
            borrowService.reserveBook(
                reserveRequest.readerRegistrationNumber,
                reserveRequest.isbn
            );
            return gson.toJson(new SuccessResponse("Book reserved successfully"));
        } catch (IllegalArgumentException e) {
            response.status(400);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private static class BorrowRequest {
        private String readerRegistrationNumber;
        private String isbn;
    }

    private static class SuccessResponse {
        private final String message;

        public SuccessResponse(String message) {
            this.message = message;
        }
    }

    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }
} 