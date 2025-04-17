package com.library.model;

public class Librarian {
    private static Librarian instance;
    private String name;

    private Librarian() {
        this.name = "Library Administrator";
    }

    public static synchronized Librarian getInstance() {
        if (instance == null) {
            instance = new Librarian();
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
} 