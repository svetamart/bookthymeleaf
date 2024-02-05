package com.example.bookthymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/books")
public class BookApiController {

    private final BookService bookService;

    @Autowired
    public BookApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> showAllBooks() {
        List<Book> allBooks = bookService.getAllBooks();
        return ResponseEntity.ok().body(allBooks);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);

        if (book != null) {
            return ResponseEntity.ok().body(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addBook")
    public ResponseEntity<String> addBook(@RequestBody Book newBook) {
        bookService.addBook(newBook);
        return ResponseEntity.ok("Book successfully added");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteBooks(@RequestParam(value = "bookIdsToDelete", required = false) List<Long> bookIdsToDelete) {
        if (bookIdsToDelete != null && !bookIdsToDelete.isEmpty()) {
            bookService.deleteBooksByIds(bookIdsToDelete);
            return ResponseEntity.ok("Books successfully deleted");
        } else {
            return ResponseEntity.badRequest().body("Choose books to delete");
        }
    }

    @PutMapping("/editBook")
    public ResponseEntity<String> editBook(@RequestBody Book editedBook) {
        bookService.editBook(editedBook.getId(), editedBook);
        return ResponseEntity.ok("Book successfully edited");
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Book>> filterBooks(@RequestBody BookFilterRequest filterRequest) {
        List<Book> filteredBooks = bookService.filterBooks(filterRequest.getAuthors(), filterRequest.getStatus(),
                filterRequest.getMinYear(), filterRequest.getMaxYear());
        return ResponseEntity.ok(filteredBooks);
    }

    @GetMapping("/uniqueAuthors")
    public ResponseEntity<List<String>> getUniqueAuthors() {
        List<String> uniqueAuthors = bookService.getUniqueAuthors();
        return ResponseEntity.ok().body(uniqueAuthors);
    }
}
