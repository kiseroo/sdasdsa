package com.library.controller;

import com.google.gson.Gson;
import com.library.model.Reader;
import com.library.service.ReaderService;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class ReaderController {
    private final ReaderService readerService;
    private final Gson gson = new Gson();

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
        setupRoutes();
    }

    private void setupRoutes() {
        get("/api/readers", this::getAllReaders);
        get("/api/readers/:registrationNumber", this::getReaderByRegistrationNumber);
        post("/api/readers", this::registerReader);
    }

    private Object getAllReaders(Request request, Response response) {
        response.type("application/json");
        return gson.toJson(readerService.getAllReaders());
    }

    private Object getReaderByRegistrationNumber(Request request, Response response) {
        response.type("application/json");
        String registrationNumber = request.params(":registrationNumber");
        try {
            Reader reader = readerService.getReaderByRegistrationNumber(registrationNumber);
            return gson.toJson(reader);
        } catch (IllegalArgumentException e) {
            response.status(404);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private Object registerReader(Request request, Response response) {
        response.type("application/json");
        try {
            ReaderRequest readerRequest = gson.fromJson(request.body(), ReaderRequest.class);
            Reader reader = readerService.registerReader(
                readerRequest.name,
                readerRequest.registrationNumber
            );
            response.status(201);
            return gson.toJson(reader);
        } catch (IllegalArgumentException e) {
            response.status(400);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private static class ReaderRequest {
        private String name;
        private String registrationNumber;
    }

    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }
} 