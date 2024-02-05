package com.example.bookthymeleaf;

import java.util.List;

public class BookFilterRequest {
    private List<String> authors;
    private List<String> status;
    private Integer minYear;
    private Integer maxYear;

    public BookFilterRequest(List<String> authors, List<String> status, Integer minYear, Integer maxYear) {
        this.authors = authors;
        this.status = status;
        this.minYear = minYear;
        this.maxYear = maxYear;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public Integer getMinYear() {
        return minYear;
    }

    public void setMinYear(Integer minYear) {
        this.minYear = minYear;
    }

    public Integer getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(Integer maxYear) {
        this.maxYear = maxYear;
    }
}
