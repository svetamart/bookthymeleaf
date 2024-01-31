package com.example.bookthymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String getAllBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/addBookForm")
    public String showAddBookForm(Model model) {
        model.addAttribute("newBook", new Book());
        return "addBookForm";
    }

    @PostMapping("/addBook")
    public String addBook(Book newBook) {
        bookService.addBook(newBook);
        return "redirect:/books";
    }
}
