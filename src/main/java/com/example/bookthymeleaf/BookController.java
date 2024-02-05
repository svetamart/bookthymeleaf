package com.example.bookthymeleaf;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/web/books")
public class BookController {

    @Value("${api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public BookController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String showAllBooks(Model model) {
        List<Book> allBooks = restTemplate.getForObject(apiUrl, List.class);
        model.addAttribute("books", allBooks);
        return "books";
    }

    @GetMapping("/addBookForm")
    public String showAddBookForm(Model model) {
        model.addAttribute("newBook", new Book());
        return "addBookForm";
    }

    @PostMapping("/addBook")
    public String addBook(@ModelAttribute Book newBook, Model model) {
        try {
            restTemplate.postForObject(apiUrl + "/addBook", new HttpEntity<>(newBook), Void.class);
            model.addAttribute("successMessage", "Book successfully added");
        } catch (RestClientException e) {
            model.addAttribute("errorMessage", "Failed to add book");
        }

        return "redirect:/web/books";
    }

    @GetMapping("/deleteBooks")
    public String showDeletePage(Model model) {
        List<Book> books = restTemplate.getForObject(apiUrl, List.class);
        model.addAttribute("books", books);
        model.addAttribute("bookIdsToDelete", new ArrayList<Long>());
        return "deleteBooks";
    }

    @PostMapping("/delete")
    public String deleteBooks(@RequestParam(value = "bookIdsToDelete", required = false) List<Long> bookIdsToDelete, Model model) {

        if (bookIdsToDelete == null || bookIdsToDelete.isEmpty()) {
            model.addAttribute("errorMessage", "Choose books to delete");
            List<Book> books = restTemplate.getForObject(apiUrl, List.class);
            model.addAttribute("books", books);
            return "deleteBooks";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Long> params = new LinkedMultiValueMap<>();
        for (Long bookId : bookIdsToDelete) {
            params.add("bookIdsToDelete", bookId);
        }

        HttpEntity<MultiValueMap<String, Long>> requestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/delete",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("successMessage", responseEntity.getBody());
        } else {
            model.addAttribute("errorMessage", responseEntity.getBody());
        }

        List<Book> books = restTemplate.getForObject(apiUrl, List.class);
        model.addAttribute("books", books);

        return "deleteBooks";
    }

    @GetMapping("/editBookForm/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        String apiEndpoint = apiUrl + "/" + id;
        Book book = restTemplate.getForObject(apiEndpoint, Book.class);
        model.addAttribute("book", book);
        return "editBookForm";
    }

    @PostMapping("/editBook")
    public String editBook(@ModelAttribute Book editedBook, Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Book> requestEntity = new HttpEntity<>(editedBook, headers);
        restTemplate.exchange(apiUrl + "/editBook", HttpMethod.PUT, requestEntity, String.class);

        return "redirect:/web/books";
    }

    @GetMapping("/filterPage")
    public String showFilterPage(Model model) {
        String apiEndpoint = apiUrl + "/uniqueAuthors";
        List<String> uniqueAuthors = restTemplate.getForObject(apiEndpoint, List.class);

        if (uniqueAuthors != null) {
            model.addAttribute("uniqueAuthors", uniqueAuthors);
        } else {
            model.addAttribute("errorMessage",
                    "Failed to retrieve unique authors.");
        }

        return "filterPage";
    }

    @PostMapping("/filter")
    public String filterBooks(@RequestParam(value = "author", required = false) List<String> authors,
                              @RequestParam(value = "status", required = false) List<String> status,
                              @RequestParam(value = "minYear", required = false) Integer minYear,
                              @RequestParam(value = "maxYear", required = false) Integer maxYear,
                              Model model) {
        BookFilterRequest filterRequest = new BookFilterRequest(authors, status, minYear, maxYear);

        List<Book> filteredBooks = restTemplate.postForObject(apiUrl + "/filter", filterRequest, List.class);

        if (filteredBooks != null) {
            model.addAttribute("books", filteredBooks);
        } else {
            model.addAttribute("errorMessage", "Error filtering books");
        }

        return "books";
    }
}
