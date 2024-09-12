package com.example.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.model.Book;
import com.example.repository.book.BookRepository;
import com.example.util.TestUtils;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    private final Book firstBook = TestUtils.getFirstBook();
    private final Book secondBook = TestUtils.getSecondBook();
    private final Book thirdBook = TestUtils.getThirdBook();

    @DisplayName("Find all books by valid categories ids")
    @Test
    @Sql(scripts = "classpath:database/01-add-three-books-with-categories-to-DB.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/02-delete-all-books-and-categories-from-DB.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllBooksByCategoryId_WithValidId_ReturnsCorrectBookList() {
        List<Book> actual = bookRepository.findAllByCategories_Id(1L, Pageable.unpaged());
        List<Book> expected = List.of(firstBook, secondBook, thirdBook);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("categories").isEqualTo(expected);

        actual = bookRepository.findAllByCategories_Id(2L, Pageable.unpaged());
        expected = List.of(secondBook);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("categories").isEqualTo(expected);
    }

    @DisplayName("Find all books by invalid categories ids")
    @Test
    @Sql(scripts = "classpath:database/01-add-three-books-with-categories-to-DB.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/02-delete-all-books-and-categories-from-DB.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllBooksByCategoryId_WithInvalidId_ReturnsEmptyBookList() {
        List<Book> actual = bookRepository.findAllByCategories_Id(5L, Pageable.unpaged());
        List<Book> expected = List.of();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
