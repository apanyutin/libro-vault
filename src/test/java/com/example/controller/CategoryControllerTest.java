package com.example.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.dto.book.BookDto;
import com.example.dto.category.CategoryDto;
import com.example.dto.category.CreateCategoryRequestDto;
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
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final BookDto secondBookDto = TestUtils.getSecondBookDto();
    private final CategoryDto firstCategoryDto = TestUtils.getFirstCategoryDto();
    private final CategoryDto secondCategoryDto = TestUtils.getSecondCategoryDto();
    private final CategoryDto thirdCategoryDto = TestUtils.getThirdCategoryDto();

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Get a list of all available categories")
    @Sql(scripts = "classpath:database/01-add-three-books-with-categories-to-DB.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/02-delete-all-books-and-categories-from-DB.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllCategories_Success() throws Exception {
        List<CategoryDto> expected = List.of(firstCategoryDto, secondCategoryDto, thirdCategoryDto);

        MvcResult result = mockMvc.perform(
                        get("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CategoryDto[].class);
        assertEquals(3, actual.length);
        assertThat(Arrays.stream(actual).toList()).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Get category by id")
    @Sql(scripts = "classpath:database/01-add-three-books-with-categories-to-DB.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/02-delete-all-books-and-categories-from-DB.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCategoryById_WithValidId_Success() throws Exception {
        CategoryDto expected = firstCategoryDto;

        MvcResult result = mockMvc.perform(
                        get("/categories/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Create a new category")
    @Sql(scripts = "classpath:database/02-delete-all-books-and-categories-from-DB.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createCategory_ValidRequestDto_Success() throws Exception {
        CreateCategoryRequestDto requestDto = TestUtils.getFirstCategoryRequestDto();
        CategoryDto expected = firstCategoryDto;
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Update category by id")
    @Sql(scripts = "classpath:database/01-add-three-books-with-categories-to-DB.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/02-delete-all-books-and-categories-from-DB.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCategory_ValidRequestDto_Success() throws Exception {
        CreateCategoryRequestDto updateRequestDto = TestUtils.getFirstCategoryRequestDto();
        updateRequestDto.setName("Update category name");
        CategoryDto expected = firstCategoryDto;
        expected.setName("Update category name");
        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/categories/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Delete category by id")
    @Sql(scripts = "classpath:database/01-add-three-books-with-categories-to-DB.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/02-delete-all-books-and-categories-from-DB.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteCategoryById_WithValidId_Success() throws Exception {
        List<CategoryDto> expected = List.of(firstCategoryDto, secondCategoryDto);

        mockMvc.perform(
                        delete("/categories/3")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        MvcResult result = mockMvc.perform(
                        get("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CategoryDto[].class);
        assertEquals(2, actual.length);
        assertThat(Arrays.stream(actual).toList()).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Get a list of books by category")
    @Sql(scripts = "classpath:database/01-add-three-books-with-categories-to-DB.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/02-delete-all-books-and-categories-from-DB.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBooksByCategory_WithValidCategoryId_ReturnsCorrectList() throws Exception {
        List<BookDto> expected = List.of(secondBookDto);

        MvcResult result = mockMvc.perform(
                        get("/categories/2/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDto[].class);
        assertEquals(1, actual.length);
        assertThat(Arrays.stream(actual).toList()).usingRecursiveComparison()
                .ignoringFields("id", "categoryIds").isEqualTo(expected);
    }
}
