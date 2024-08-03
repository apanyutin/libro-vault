package com.example;

import com.example.model.Book;
import com.example.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setAuthor("Pushkin");
            book.setIsbn("isbn");
            book.setPrice(BigDecimal.valueOf(150));
            book.setTitle("Fairy tales");

            bookService.save(book);

            System.out.println(bookService.findAll());
        };
    }
}
