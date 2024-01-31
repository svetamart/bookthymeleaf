package com.example.bookthymeleaf;

import java.util.List;

public interface BookRepo {
        List<Book> getAllBooks();
        void addBook(Book book);

}
