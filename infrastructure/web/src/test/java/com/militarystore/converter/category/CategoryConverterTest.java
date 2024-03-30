package com.militarystore.converter.category;

import com.militarystore.entity.category.Category;
import com.militarystore.entity.subcategory.Subcategory;
import com.militarystore.model.dto.category.CategoryDto;
import com.militarystore.model.dto.category.SubcategoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryConverterTest {

    private CategoryConverter categoryConverter;

    @BeforeEach
    void setUp() {
        categoryConverter = new CategoryConverter();
    }

    @Test
    void convertToCategoryDto() {
        var category = Category.builder()
            .id(1)
            .name("category")
            .build();

        var expected = CategoryDto.builder()
            .id(1)
            .name("category")
            .build();

        assertThat(categoryConverter.convertToCategoryDto(category)).isEqualTo(expected);
    }

    @Test
    void convertToCategory() {
        var expected = Category.builder()
            .name("category")
            .build();

        assertThat(categoryConverter.convertToCategory("category")).isEqualTo(expected);
    }

    @Test
    void convertToCategoryWithId() {
        var expected = Category.builder()
            .id(1)
            .name("category")
            .build();

        assertThat(categoryConverter.convertToCategory(1, "category")).isEqualTo(expected);
    }

    @Test
    void convertToSubcategoryDto() {
        var subcategory = Subcategory.builder()
            .id(1)
            .name("subcategory")
            .build();

        var expected = SubcategoryDto.builder()
            .id(1)
            .name("subcategory")
            .build();

        assertThat(categoryConverter.convertToSubcategoryDto(subcategory)).isEqualTo(expected);
    }

    @Test
    void convertToSubcategory() {
        var expected = Subcategory.builder()
            .name("subcategory")
            .categoryId(1)
            .build();

        assertThat(categoryConverter.convertToSubcategory(1, "subcategory")).isEqualTo(expected);
    }

    @Test
    void convertToSubcategoryWithId() {
        var expected = Subcategory.builder()
            .id(1)
            .name("subcategory")
            .categoryId(1)
            .build();

        assertThat(categoryConverter.convertToSubcategory(1, 1, "subcategory")).isEqualTo(expected);
    }
}