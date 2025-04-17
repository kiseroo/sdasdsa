package com.library.model;

import java.util.ArrayList;
import java.util.List;

public class Reader {
    private String name;
    private String registrationNumber;
    private List<Book> borrowedBooks;
    private List<String> borrowHistory;

    public Reader(String name, String registrationNumber) {
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.borrowedBooks = new ArrayList<>();
        this.borrowHistory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public boolean borrowBook(Book book, String borrowDate) {
        if (borrowedBooks.size() >= 5) {
            return false;
        }
        borrowedBooks.add(book);
        borrowHistory.add(book.getIsbn() + "," + borrowDate);
        return true;
    }

    public boolean returnBook(Book book) {
        return borrowedBooks.remove(book);
    }

    public List<String> getBorrowHistory() {
        return borrowHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return registrationNumber.equals(reader.registrationNumber);
    }

    @Override
    public int hashCode() {
        return registrationNumber.hashCode();
    }
} 