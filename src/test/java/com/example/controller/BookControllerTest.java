package com.example.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.dto.book.BookDto;
import com.example.dto.book.CreateBookRequestDto;
import com.example.model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
    private final BookDto expectedBookDto = new BookDto();
    private final BookDto firstBookDto = new BookDto();
    private final BookDto secondBookDto = new BookDto();
    private final BookDto thirdBookDto = new BookDto();

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp() {
        bookRequestDto.setTitle("Book Title");
        bookRequestDto.setPrice(BigDecimal.valueOf(102.99));
        bookRequestDto.setIsbn("978-9-866-21156-9");
        bookRequestDto.setAuthor("Book Author");
        bookRequestDto.setDescription("Book Description");
        bookRequestDto.setCoverImage("Cover image of book");
        bookRequestDto.setCategoryIds(Set.of(3L));

        expectedBookDto.setId(1L);
        expectedBookDto.setTitle(bookRequestDto.getTitle());
        expectedBookDto.setPrice(bookRequestDto.getPrice());
        expectedBookDto.setIsbn(bookRequestDto.getIsbn());
        expectedBookDto.setAuthor(bookRequestDto.getAuthor());
        expectedBookDto.setDescription(bookRequestDto.getDescription());
        expectedBookDto.setCoverImage(bookRequestDto.getCoverImage());
        expectedBookDto.setCategoryIds(bookRequestDto.getCategoryIds());

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

        firstBookDto.setId(1L);
        firstBookDto.setTitle("First book Title");
        firstBookDto.setPrice(BigDecimal.valueOf(102.99));
        firstBookDto.setIsbn("978-9-166-21156-9");
        firstBookDto.setAuthor("First book Author");
        firstBookDto.setDescription("First book Description");
        firstBookDto.setCoverImage("Cover image of first book");
        firstBookDto.setCategoryIds(Set.of(firstCategory.getId()));

        secondBookDto.setId(2L);
        secondBookDto.setTitle("Second book Title");
        secondBookDto.setPrice(BigDecimal.valueOf(922.55));
        secondBookDto.setIsbn("978-9-266-21156-9");
        secondBookDto.setAuthor("Second book Author");
        secondBookDto.setDescription("Second book Description");
        secondBookDto.setCoverImage("Cover image of second book");
        secondBookDto.setCategoryIds(Set.of(firstCategory.getId(), secondCategory.getId()));

        thirdBookDto.setId(3L);
        thirdBookDto.setTitle("Third book Title");
        thirdBookDto.setPrice(BigDecimal.valueOf(12.05));
        thirdBookDto.setIsbn("978-9-366-21156-9");
        thirdBookDto.setAuthor("Third book Author");
        thirdBookDto.setDescription("Third book Description");
        thirdBookDto.setCoverImage("Cover image of third book");
        thirdBookDto.setCategoryIds(Set.of(firstCategory.getId(), thirdCategory.getId()));
    }

    @Sql(scripts = "classpath:database/01-add-three-books-with-categories-to-DB.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/02-delete-all-books-and-categories-from-DB.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user")
    @Test
    void getAllBooks_ReturnsCorrectBookDtoList() throws Exception {
        List<BookDto> expected = List.of(firstBookDto, secondBookDto, thirdBookDto);

        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto[].class);
        Assertions.assertNotNull(actual);
        assertThat(Arrays.stream(actual).toList()).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(expected);
    }

    @DisplayName("Verify the correct BookDto is returned when get book by valid id")
    @Sql(scripts = "classpath:database/01-add-three-books-with-categories-to-DB.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/02-delete-all-books-and-categories-from-DB.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user")
    @Test
    void getBookById_WithValidId_ReturnsCorrectBookDto() throws Exception {
        BookDto expected = firstBookDto;

        MvcResult result = mockMvc.perform(
                        get("/books/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

    @DisplayName("Verify the 404 answer is returned when get book by valid id")
    @Sql(scripts = "classpath:database/01-add-three-books-with-categories-to-DB.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/02-delete-all-books-and-categories-from-DB.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user")
    @Test
    void getBookById_WithInvalidId_Returns404Answer() throws Exception {
        long nonExistingId = 5L;

        MvcResult result = mockMvc.perform(
                        get("/books/" + nonExistingId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseContent.contains(
                "DB doesn't have book with id = " + nonExistingId));
    }

    @DisplayName("Verify the correct bookDto is returned when create book")
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    void createBook_WithValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actualBookDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        assertThat(actualBookDto).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(expectedBookDto);
    }

    @DisplayName("Verify the book is deleted when delete book by valid id")
    @Sql(scripts = "classpath:database/01-add-three-books-with-categories-to-DB.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/02-delete-all-books-and-categories-from-DB.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    void deleteBookById_WithValidId_Success() throws Exception {
        List<BookDto> expected = List.of(firstBookDto, secondBookDto);

        mockMvc.perform(
                        delete("/books/3")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(2, actual.length);
        assertThat(Arrays.stream(actual).toList()).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(expected);
    }

    @DisplayName("Verify the book is updated when update book by valid id")
    @Sql(scripts = "classpath:database/01-add-three-books-with-categories-to-DB.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/02-delete-all-books-and-categories-from-DB.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    void updateBook() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/books/3")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actualBookDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        assertThat(actualBookDto).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(expectedBookDto);
    }
}
