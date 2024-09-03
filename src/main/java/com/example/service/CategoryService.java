package com.example.service;

import com.example.dto.category.CategoryDto;
import com.example.dto.category.CreateCategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryRequestDto requestDto);

    CategoryDto updateById(CreateCategoryRequestDto requestDto, Long id);

    void deleteById(Long id);
}
