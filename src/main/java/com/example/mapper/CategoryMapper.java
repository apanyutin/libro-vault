package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.category.CategoryDto;
import com.example.dto.category.CreateCategoryRequestDto;
import com.example.model.Category;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    List<CategoryDto> toDto(List<Category> categories);

    Category toModel(CreateCategoryRequestDto requestDto);

    void updateCategoryFromDto(
            @MappingTarget Category category, CreateCategoryRequestDto requestDto);
}
