package com.example.bookthymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final BookRepo bookRepository;

    @Autowired
    public BookService(BookRepo bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void addBook(Book book) {
        bookRepository.save(book);
    }

    public void deleteBooksByIds(List<Long> bookIds) {
        bookRepository.deleteAllById(bookIds);
    }

    public void editBook(Long id, Book book) {
        Book existingBook = getBookById(id);
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setYear(book.getYear());
        existingBook.setStatus(book.getStatus());

        bookRepository.save(existingBook);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).get();
    }

    public List<Book> filterBooks(List<String> authors, List<String> status, Integer minYear, Integer maxYear) {
        List<Book> filteredBooks = new ArrayList<>();

        if (authors != null && !authors.isEmpty()) {
            filteredBooks.addAll(bookRepository.findByAuthorIn(authors));
        } else {
            filteredBooks.addAll(bookRepository.findAll());
        }

        if (status != null && !status.isEmpty()) {
            filteredBooks.retainAll(bookRepository.findByStatusIn(status));
        }

        if (minYear != null && maxYear != null) {
            filteredBooks.retainAll(bookRepository.findByYearBetween(minYear, maxYear));
        } else if (minYear != null) {
            filteredBooks.retainAll(bookRepository.findByYearGreaterThanEqual(minYear));
        } else if (maxYear != null) {
            filteredBooks.retainAll(bookRepository.findByYearLessThanEqual(maxYear));
        }

        return filteredBooks;
    }

    public List<String> getUniqueAuthors () {
        return bookRepository.findDistinctAuthors();
    }
}
