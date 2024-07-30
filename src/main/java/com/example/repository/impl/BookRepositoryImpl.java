package com.example.repository.impl;

import com.example.model.Book;
import com.example.repository.BookRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public List findAll() {
        return null;
    }
}
