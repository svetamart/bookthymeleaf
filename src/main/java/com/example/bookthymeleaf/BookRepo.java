package com.example.bookthymeleaf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    @Query("SELECT DISTINCT b.author FROM Book b")
    List<String> findDistinctAuthors();

    List<Book> findByAuthorIn(List<String> authors);
    List<Book> findByStatusIn(List<String> statusList);
    List<Book> findByYearBetween(Integer minYear, Integer maxYear);
    List<Book> findByYearGreaterThanEqual(Integer minYear);
    List<Book> findByYearLessThanEqual(Integer maxYear);

}
