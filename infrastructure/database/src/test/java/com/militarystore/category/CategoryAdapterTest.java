package com.militarystore.category;

import com.militarystore.category.mapper.CategoryMapper;
import com.militarystore.entity.category.Category;
import com.militarystore.jooq.tables.records.CategoriesRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryAdapterTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    private CategoryAdapter categoryAdapter;

    @BeforeEach
    void setUp() {
        categoryAdapter = new CategoryAdapter(categoryRepository, categoryMapper);
    }

    @Test
    void addCategory() {
        var category = Category.builder()
            .name("name")
            .build();

        categoryAdapter.addCategory(category);

        verify(categoryRepository).addCategory(category);
    }

    @Test
    void updateCategory() {
        var category = Category.builder()
            .id(1)
            .name("name")
            .build();

        categoryAdapter.updateCategory(category);

        verify(categoryRepository).updateCategory(category);
    }

    @Test
    void deleteCategory() {
        categoryAdapter.deleteCategory(1);

        verify(categoryRepository).deleteCategory(1);
    }

    @Test
    void getCategories() {
        var categoryRecord = new CategoriesRecord();
        var category = Category.builder()
            .id(1)
            .name("name")
            .build();

        when(categoryRepository.getCategories()).thenReturn(List.of(categoryRecord));
        when(categoryMapper.map(categoryRecord)).thenReturn(category);

        assertThat(categoryAdapter.getCategories()).isEqualTo(List.of(category));
    }
}