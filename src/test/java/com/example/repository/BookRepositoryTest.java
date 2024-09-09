package com.example.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.model.Book;
import com.example.model.Category;
import com.example.repository.book.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
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
    private final Book firstBook = new Book();
    private final Book secondBook = new Book();
    private final Book thirdBook = new Book();

    @BeforeEach
    void setUp() {
        //This setUp is used with file: database/01-add-three-books-with-categories-to-DB.sql
        Category firstCategory = new Category();
        firstCategory.setName("First category name");
        firstCategory.setId(1L);

        Category secondCategory = new Category();
        secondCategory.setName("Second category Name");
        secondCategory.setId(2L);

        Category thirdCategory = new Category();
        thirdCategory.setName("Third category Name");
        thirdCategory.setId(3L);

        firstBook.setId(1L);
        firstBook.setTitle("First book Title");
        firstBook.setPrice(BigDecimal.valueOf(102.99));
        firstBook.setIsbn("978-9-166-21156-9");
        firstBook.setAuthor("First book Author");
        firstBook.setDescription("First book Description");
        firstBook.setCoverImage("Cover image of first book");
        firstBook.setCategories(Set.of(firstCategory));

        secondBook.setId(2L);
        secondBook.setTitle("Second book Title");
        secondBook.setPrice(BigDecimal.valueOf(922.55));
        secondBook.setIsbn("978-9-266-21156-9");
        secondBook.setAuthor("Second book Author");
        secondBook.setDescription("Second book Description");
        secondBook.setCoverImage("Cover image of second book");
        secondBook.setCategories(Set.of(firstCategory, secondCategory));

        thirdBook.setId(3L);
        thirdBook.setTitle("Third book Title");
        thirdBook.setPrice(BigDecimal.valueOf(12.05));
        thirdBook.setIsbn("978-9-366-21156-9");
        thirdBook.setAuthor("Third book Author");
        thirdBook.setDescription("Third book Description");
        thirdBook.setCoverImage("Cover image of third book");
        thirdBook.setCategories(Set.of(firstCategory, thirdCategory));
    }

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
