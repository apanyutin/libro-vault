package com.example.dto.book;

public record BookSearchParameters(String[] authors, String[] wordsFromTitle) {
}
