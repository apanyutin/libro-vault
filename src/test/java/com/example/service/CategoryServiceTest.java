package com.example.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.dto.category.CategoryDto;
import com.example.dto.category.CreateCategoryRequestDto;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.CategoryMapper;
import com.example.mapper.impl.CategoryMapperImpl;
import com.example.model.Category;
import com.example.repository.category.CategoryRepository;
import com.example.service.impl.CategoryServiceImpl;
import com.example.util.TestUtils;
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
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    private final CategoryMapper categoryMapper = new CategoryMapperImpl();
    private final Category category = TestUtils.getFirstCategory();
    private final CategoryDto expectedCategoryDto = TestUtils.getFirstCategoryDto();
    private final CreateCategoryRequestDto categoryRequestDto = TestUtils
            .getFirstCategoryRequestDto();

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryMapper, categoryRepository);
    }

    @DisplayName("Verify the correct list of CategoryDto is returned when get all categories")
    @Test
    void findAllCategories_ReturnsCorrectCategoryList() {
        Page<Category> categoryPage = new PageImpl<Category>(List.of(category));

        Mockito.when(categoryRepository.findAll(Pageable.unpaged())).thenReturn(categoryPage);
        List<CategoryDto> actualCategoryDtoList = categoryService.findAll(Pageable.unpaged());

        List<CategoryDto> expectedCategoryDtoList = List.of(expectedCategoryDto);
        assertThat(actualCategoryDtoList).usingRecursiveComparison()
                .isEqualTo(expectedCategoryDtoList);

    }

    @DisplayName("Verify the correct CategoryDto is returned when get by valid id")
    @Test
    void getCategoryById_WithValidId_ReturnsCorrectCategoryDto() {
        Mockito.when(categoryRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(category));

        CategoryDto actualCategoryDto = categoryService.getById(category.getId());
        assertThat(actualCategoryDto).usingRecursiveComparison().isEqualTo(expectedCategoryDto);
    }

    @DisplayName("Verify the EntityNotFoundException is returned when get category by invalid id")
    @Test
    void getCategoryById_WithInvalidId_ReturnsEntityNotFoundException() {
        Mockito.when(categoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(Mockito.anyLong()));
    }

    @DisplayName("Verify the correct categoryDto is returned when save valid category")
    @Test
    void saveCategory_WithCorrectFields_ReturnsCorrectCategoryDto() {
        Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);
        CategoryDto actualCategoryDto = categoryService.save(categoryRequestDto);

        assertThat(actualCategoryDto).usingRecursiveComparison().isEqualTo(expectedCategoryDto);
    }

    @DisplayName("Verify the correct categoryDto is returned when update category by id")
    @Test
    void updateById() {
        CreateCategoryRequestDto updateCategoryRequestDto = categoryRequestDto;
        updateCategoryRequestDto.setName("Updated category name");

        Mockito.when(categoryRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(category));
        CategoryDto actual = categoryService.updateById(updateCategoryRequestDto, category.getId());

        CategoryDto expected = expectedCategoryDto;
        expected.setName("Updated category name");
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
