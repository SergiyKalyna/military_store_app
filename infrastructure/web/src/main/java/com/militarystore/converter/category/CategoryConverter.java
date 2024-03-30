package com.militarystore.converter.category;

import com.militarystore.entity.category.Category;
import com.militarystore.entity.subcategory.Subcategory;
import com.militarystore.model.dto.category.CategoryDto;
import com.militarystore.model.dto.category.SubcategoryDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {

    public CategoryDto convertToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.id())
                .name(category.name())
                .build();
    }

    public Category convertToCategory(String categoryName) {
        return Category.builder()
                .name(categoryName)
                .build();
    }

    public Category convertToCategory(int categoryId, String categoryName) {
        return Category.builder()
            .id(categoryId)
            .name(categoryName)
            .build();
    }

    public SubcategoryDto convertToSubcategoryDto(Subcategory subcategory) {
        return SubcategoryDto.builder()
            .id(subcategory.id())
            .name(subcategory.name())
            .categoryId(subcategory.categoryId())
            .build();
    }

    public Subcategory convertToSubcategory(int categoryId, String subcategoryName) {
        return Subcategory.builder()
            .name(subcategoryName)
            .categoryId(categoryId)
            .build();
    }

    public Subcategory convertToSubcategory(int categoryId, int subcategoryId, String subcategoryName) {
        return Subcategory.builder()
            .id(subcategoryId)
            .name(subcategoryName)
            .categoryId(categoryId)
            .build();
    }
}
