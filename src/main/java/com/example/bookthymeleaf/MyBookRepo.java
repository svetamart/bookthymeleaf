package com.example.bookthymeleaf;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class MyBookRepo implements BookRepo{

    private List<Book> books = new ArrayList<>();

    @Override
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    @Override
    public void addBook(Book book) {
        book.setId(generateUniqueId());
        books.add(book);
    }

    private Long generateUniqueId() {
        return System.currentTimeMillis();
    }
}
