package com.example.util;

import com.example.dto.book.BookDto;
import com.example.dto.book.CreateBookRequestDto;
import com.example.dto.category.CategoryDto;
import com.example.dto.category.CreateCategoryRequestDto;
import com.example.model.Book;
import com.example.model.Category;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

//These methods are used with file: database/01-add-three-books-with-categories-to-DB.sql.
//These objects are equivalent to the records in the table after the script:
// database/01-add-three-books-with-categories-to-DB.sql.
public class TestUtils {
    public static Category getFirstCategory() {
        Category firstCategory = new Category();
        firstCategory.setName("First category Name");
        firstCategory.setDescription("First category Description");
        firstCategory.setId(1L);
        return firstCategory;
    }

    public static Category getSecondCategory() {
        Category secondCategory = new Category();
        secondCategory.setName("Second category Name");
        secondCategory.setDescription("Second category Description");
        secondCategory.setId(2L);
        return secondCategory;
    }

    public static Category getThirdCategory() {
        Category thirdCategory = new Category();
        thirdCategory.setName("Third category Name");
        thirdCategory.setDescription("Third category Description");
        thirdCategory.setId(3L);
        return thirdCategory;
    }

    public static Book getFirstBook() {
        Book firstBook = new Book();
        firstBook.setId(1L);
        firstBook.setTitle("First book Title");
        firstBook.setPrice(BigDecimal.valueOf(102.99));
        firstBook.setIsbn("978-9-166-21156-9");
        firstBook.setAuthor("First book Author");
        firstBook.setDescription("First book Description");
        firstBook.setCoverImage("Cover image of first book");
        firstBook.setCategories(Set.of(getFirstCategory()));
        return firstBook;
    }

    public static Book getSecondBook() {
        Book secondBook = new Book();
        secondBook.setId(2L);
        secondBook.setTitle("Second book Title");
        secondBook.setPrice(BigDecimal.valueOf(922.55));
        secondBook.setIsbn("978-9-266-21156-9");
        secondBook.setAuthor("Second book Author");
        secondBook.setDescription("Second book Description");
        secondBook.setCoverImage("Cover image of second book");
        secondBook.setCategories(Set.of(getFirstCategory(), getSecondCategory()));
        return secondBook;
    }

    public static Book getThirdBook() {
        Book thirdBook = new Book();
        thirdBook.setId(3L);
        thirdBook.setTitle("Third book Title");
        thirdBook.setPrice(BigDecimal.valueOf(12.05));
        thirdBook.setIsbn("978-9-366-21156-9");
        thirdBook.setAuthor("Third book Author");
        thirdBook.setDescription("Third book Description");
        thirdBook.setCoverImage("Cover image of third book");
        thirdBook.setCategories(Set.of(getFirstCategory(), getThirdCategory()));
        return thirdBook;
    }

    public static BookDto getFirstBookDto() {
        Book firstBook = getFirstBook();
        BookDto firstBookDto = new BookDto();
        firstBookDto.setId(firstBook.getId());
        firstBookDto.setTitle(firstBook.getTitle());
        firstBookDto.setPrice(firstBook.getPrice());
        firstBookDto.setIsbn(firstBook.getIsbn());
        firstBookDto.setAuthor(firstBook.getAuthor());
        firstBookDto.setDescription(firstBook.getDescription());
        firstBookDto.setCoverImage(firstBook.getCoverImage());
        firstBookDto.setCategoryIds(firstBook.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
        return firstBookDto;
    }

    public static BookDto getSecondBookDto() {
        Book secondBook = getSecondBook();
        BookDto secondBookDto = new BookDto();
        secondBookDto.setId(secondBook.getId());
        secondBookDto.setTitle(secondBook.getTitle());
        secondBookDto.setPrice(secondBook.getPrice());
        secondBookDto.setIsbn(secondBook.getIsbn());
        secondBookDto.setAuthor(secondBook.getAuthor());
        secondBookDto.setDescription(secondBook.getDescription());
        secondBookDto.setCoverImage(secondBook.getCoverImage());
        secondBookDto.setCategoryIds(secondBook.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
        return secondBookDto;
    }

    public static BookDto getThirdBookDto() {
        Book thirdBook = getThirdBook();
        BookDto thirdBookDto = new BookDto();
        thirdBookDto.setId(thirdBook.getId());
        thirdBookDto.setTitle(thirdBook.getTitle());
        thirdBookDto.setPrice(thirdBook.getPrice());
        thirdBookDto.setIsbn(thirdBook.getIsbn());
        thirdBookDto.setAuthor(thirdBook.getAuthor());
        thirdBookDto.setDescription(thirdBook.getDescription());
        thirdBookDto.setCoverImage(thirdBook.getCoverImage());
        thirdBookDto.setCategoryIds(thirdBook.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
        return thirdBookDto;
    }
    
    public static CreateBookRequestDto getFirstBookRequestDto() {
        Book firstBook = getFirstBook();
        CreateBookRequestDto firstBookRequestDto = new CreateBookRequestDto();
        firstBookRequestDto.setTitle(firstBook.getTitle());
        firstBookRequestDto.setPrice(firstBook.getPrice());
        firstBookRequestDto.setIsbn(firstBook.getIsbn());
        firstBookRequestDto.setAuthor(firstBook.getAuthor());
        firstBookRequestDto.setDescription(firstBook.getDescription());
        firstBookRequestDto.setCoverImage(firstBook.getCoverImage());
        firstBookRequestDto.setCategoryIds(firstBook.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
        return firstBookRequestDto;
    }

    public static CategoryDto getFirstCategoryDto() {
        Category firstCategory = getFirstCategory();
        CategoryDto firstCategoryDto = new CategoryDto();
        firstCategoryDto.setName(firstCategory.getName());
        firstCategoryDto.setDescription(firstCategory.getDescription());
        firstCategoryDto.setId(firstCategory.getId());
        return firstCategoryDto;
    }

    public static CategoryDto getSecondCategoryDto() {
        Category secondCategory = getSecondCategory();
        CategoryDto secondCategoryDto = new CategoryDto();
        secondCategoryDto.setName(secondCategory.getName());
        secondCategoryDto.setDescription(secondCategory.getDescription());
        secondCategoryDto.setId(secondCategory.getId());
        return secondCategoryDto;
    }

    public static CategoryDto getThirdCategoryDto() {
        Category thirdCategory = getThirdCategory();
        CategoryDto thirdCategoryDto = new CategoryDto();
        thirdCategoryDto.setName(thirdCategory.getName());
        thirdCategoryDto.setDescription(thirdCategory.getDescription());
        thirdCategoryDto.setId(thirdCategory.getId());
        return thirdCategoryDto;
    }

    public static CreateCategoryRequestDto getFirstCategoryRequestDto() {
        Category firstCategory = getFirstCategory();
        CreateCategoryRequestDto firstCategoryRequestDto = new CreateCategoryRequestDto();
        firstCategoryRequestDto.setName(firstCategory.getName());
        firstCategoryRequestDto.setDescription(firstCategory.getDescription());
        return firstCategoryRequestDto;
    }
}
