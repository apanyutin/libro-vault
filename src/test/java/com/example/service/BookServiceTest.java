package com.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.example.dto.book.BookDto;
import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.book.BookSearchParameters;
import com.example.dto.book.CreateBookRequestDto;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.BookMapper;
import com.example.mapper.impl.BookMapperImpl;
import com.example.model.Book;
import com.example.repository.book.BookRepository;
import com.example.repository.book.BookSpecificationBuilder;
import com.example.service.impl.BookServiceImpl;
import com.example.util.TestUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;
    private final BookMapper bookMapper = new BookMapperImpl();
    private final Book book = TestUtils.getFirstBook();
    private final BookDto expectedBookDto = TestUtils.getFirstBookDto();
    private final CreateBookRequestDto bookRequestDto = TestUtils.getFirstBookRequestDto();

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(bookRepository, bookMapper, bookSpecificationBuilder);
    }

    @DisplayName("Verify the correct BookDto is returned when correct Book ia saved")
    @Test
    void saveBook_WithCorrectBookFields_ReturnsCorrectBookDto() {
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);
        BookDto actualBookDto = bookService.save(bookRequestDto);

        assertThat(actualBookDto).usingRecursiveComparison().isEqualTo(expectedBookDto);
    }

    @DisplayName("Verify the correct list of BookDto is returned when get all books")
    @Test
    void getAllBooks_ReturnsCorrectBookList() {
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books);

        Mockito.when(bookRepository.findAll(Pageable.unpaged())).thenReturn(bookPage);
        List<BookDto> actualBookDtoList = bookService.getAll(Pageable.unpaged());

        List<BookDto> expectedBookDtoList = List.of(expectedBookDto);
        assertThat(actualBookDtoList).usingRecursiveComparison().isEqualTo(expectedBookDtoList);
    }

    @DisplayName("Verify the correct BookDto is returned when try get by id")
    @Test
    void getBookById_WithValidId_ReturnsCorrectBookDto() {
        Long bookId = book.getId();

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        BookDto actualBookDto = bookService.getById(bookId);

        assertThat(actualBookDto).usingRecursiveComparison().isEqualTo(expectedBookDto);
    }

    @DisplayName("Verify the EntityNotFoundException is returned when get by invalid id")
    @Test
    void getBookById_WithInvalidId_ReturnsEntityNotFoundException() {
        Mockito.when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.getById(Mockito.anyLong()));
    }

    @DisplayName("Verify the correct BookDto is returned when update book by id")
    @Test
    void updateBookById_WithValidRequest_ReturnsCorrectBookDto() {
        CreateBookRequestDto updateBookRequestDto = bookRequestDto;
        updateBookRequestDto.setTitle("Updated Title");

        Mockito.when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(book));
        BookDto actual = bookService.updateById(updateBookRequestDto, book.getId());

        BookDto expected = expectedBookDto;
        expected.setTitle("Updated Title");
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Verify the correct List of BookDto is returned when search by params")
    @Test
    void searchBooksByParams_WithValidParams_ReturnsCorrectBookList() {
        Specification<Book> bookSpecification = mock(Specification.class);
        BookSearchParameters searchParams = new BookSearchParameters(null, null);
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(bookSpecificationBuilder.build(Mockito.eq(searchParams)))
                .thenReturn(bookSpecification);
        Mockito.when(bookRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(book)));
        List<BookDto> actualBookDtoList = bookService.search(searchParams, pageable);

        List<BookDto> expectedBookDtoList = List.of(expectedBookDto);
        assertThat(actualBookDtoList).usingRecursiveComparison().isEqualTo(expectedBookDtoList);
    }

    @DisplayName("Verify the correct BookDto list is returned when get by category id")
    @Test
    void getAllBookByCategoryId_WithValidId_ReturnsCorrectBookList() {
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds.setTitle(expectedBookDto.getTitle());
        bookDtoWithoutCategoryIds.setPrice(expectedBookDto.getPrice());
        bookDtoWithoutCategoryIds.setIsbn(expectedBookDto.getIsbn());
        bookDtoWithoutCategoryIds.setAuthor(expectedBookDto.getAuthor());
        bookDtoWithoutCategoryIds.setDescription(expectedBookDto.getDescription());
        bookDtoWithoutCategoryIds.setCoverImage(expectedBookDto.getCoverImage());
        bookDtoWithoutCategoryIds.setId(expectedBookDto.getId());
        List<Book> books = List.of(book);
        Long categoryId = book.getCategories().stream()
                .findFirst()
                .get()
                .getId();

        Mockito.when(bookRepository.findAllByCategories_Id(
                categoryId, Pageable.unpaged())).thenReturn(books);
        List<BookDtoWithoutCategoryIds> actual = bookService.getAllByCategoryId(
                categoryId, Pageable.unpaged());

        List<BookDtoWithoutCategoryIds> expected = List.of(bookDtoWithoutCategoryIds);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("Verify the empty BookDto list is returned when get by invalid category id")
    @Test
    void getAllBookByCategoryId_WithInvalidId_ReturnsEmptyList() {
        List<Book> books = new ArrayList<>();
        Mockito.when(bookRepository.findAllByCategories_Id(
                Mockito.anyLong(), Mockito.eq(Pageable.unpaged()))).thenReturn(books);
        List<BookDtoWithoutCategoryIds> actual = bookService
                .getAllByCategoryId(5L, Pageable.unpaged());

        Assertions.assertEquals(0, actual.size());
    }
}
