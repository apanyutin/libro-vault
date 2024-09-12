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
import com.example.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
    private final CreateBookRequestDto bookRequestDto = TestUtils.getFirstBookRequestDto();
    private final BookDto expectedBookDto = TestUtils.getFirstBookDto();
    private final BookDto firstBookDto = TestUtils.getFirstBookDto();
    private final BookDto secondBookDto = TestUtils.getSecondBookDto();
    private final BookDto thirdBookDto = TestUtils.getThirdBookDto();

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
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
    @Sql(scripts = "classpath:database/02-delete-all-books-and-categories-from-DB.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
        CreateBookRequestDto updateBookRequestDto = bookRequestDto;
        updateBookRequestDto.setTitle("Update book Title");
        updateBookRequestDto.setIsbn("978-9-866-21156-9");
        String jsonRequest = objectMapper.writeValueAsString(updateBookRequestDto);
        BookDto expected = expectedBookDto;
        expectedBookDto.setTitle(updateBookRequestDto.getTitle());
        expectedBookDto.setIsbn(updateBookRequestDto.getIsbn());

        MvcResult result = mockMvc.perform(
                        put("/books/3")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(expected);
    }
}
