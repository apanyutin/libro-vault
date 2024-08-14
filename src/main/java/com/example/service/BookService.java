package com.example.service;

import com.example.dto.BookDto;
import com.example.dto.BookSearchParameters;
import com.example.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> getAll();

    BookDto getById(Long id);

    void deleteById(Long id);

    BookDto updateById(CreateBookRequestDto requestDto, Long id);

    List<BookDto> search(BookSearchParameters params);
}
