package com.militarystore.category;

import com.militarystore.entity.category.Category;
import com.militarystore.entity.subcategory.Subcategory;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.out.category.CategoryPort;
import com.militarystore.port.out.subcategory.SubcategoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryPort categoryPort;

    @Mock
    private SubcategoryPort subcategoryPort;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryPort, subcategoryPort);
    }

    @Test
    void addCategory_whenCategoryNameIsNull_shouldThrowValidationException() {
        var category = Category.builder().build();

        assertThrows(MsValidationException.class, () -> categoryService.addCategory(category));
    }

    @Test
    void addCategory_whenCategoryNameIsEmpty_shouldThrowValidationException() {
        var category = Category.builder().name("  ").build();

        assertThrows(MsValidationException.class, () -> categoryService.addCategory(category));
    }

    @Test
    void addCategory_whenCategoryIsValid_shouldAddCategory() {
        var category = Category.builder().name("category").build();

        categoryService.addCategory(category);

        verify(categoryPort).addCategory(category);
    }

    @Test
    void updateCategory_whenCategoryDoesNotExist_shouldThrowNotFoundException() {
        var category = Category.builder().id(1).name("category").build();

        when(categoryPort.isCategoryExists(category.id())).thenReturn(false);

        assertThrows(MsNotFoundException.class, () -> categoryService.updateCategory(category));
    }

    @Test
    void updateCategory_whenCategoryIsValid_shouldUpdateCategory() {
        var category = Category.builder().id(1).name("category").build();

        when(categoryPort.isCategoryExists(category.id())).thenReturn(true);

        categoryService.updateCategory(category);

        verify(categoryPort).updateCategory(category);
    }

    @Test
    void updateCategory_whenCategoryNameIsEmpty_shouldThrowValidationException() {
        var category = Category.builder().id(1).name("  ").build();

        when(categoryPort.isCategoryExists(1)).thenReturn(true);

        assertThrows(MsValidationException.class, () -> categoryService.updateCategory(category));
    }

    @Test
    void updateCategory_whenCategoryNameIsNull_shouldThrowValidationException() {
        var category = Category.builder().id(1).build();

        when(categoryPort.isCategoryExists(1)).thenReturn(true);

        assertThrows(MsValidationException.class, () -> categoryService.updateCategory(category));
    }

    @Test
    void deleteCategory_whenCategoryDoesNotExist_shouldThrowNotFoundException() {
        var categoryId = 1;

        when(categoryPort.isCategoryExists(categoryId)).thenReturn(false);

        assertThrows(MsNotFoundException.class, () -> categoryService.deleteCategory(categoryId));
    }

    @Test
    void deleteCategory_whenCategoryHasSubcategories_shouldThrowValidationException() {
        var categoryId = 1;
        var subcategories = List.of(Subcategory.builder().build());

        when(categoryPort.isCategoryExists(categoryId)).thenReturn(true);
        when(subcategoryPort.getSubcategoriesByCategoryId(categoryId)).thenReturn(subcategories);

        assertThrows(MsValidationException.class, () -> categoryService.deleteCategory(categoryId));
    }

    @Test
    void deleteCategory_whenCategoryIsValid_shouldDeleteCategory() {
        var categoryId = 1;

        when(categoryPort.isCategoryExists(categoryId)).thenReturn(true);
        when(subcategoryPort.getSubcategoriesByCategoryId(categoryId)).thenReturn(List.of());

        categoryService.deleteCategory(categoryId);

        verify(categoryPort).deleteCategory(categoryId);
    }

    @Test
    void getCategories_shouldReturnCategories() {
        var category = Category.builder().build();

        when(categoryPort.getCategories()).thenReturn(List.of(category));

        assertThat(categoryService.getCategories()).isEqualTo(List.of(category));
    }

    @Test
    void getCategoryById_whenCategoryDoesNotExist_shouldThrowNotFoundException() {
        var categoryId = 1;

        when(categoryPort.isCategoryExists(categoryId)).thenReturn(false);

        assertThrows(MsNotFoundException.class, () -> categoryService.getCategoryById(categoryId));
    }

    @Test
    void getCategoryById_whenCategoryExists_shouldReturnCategory() {
        var categoryId = 1;
        var category = Category.builder().build();

        when(categoryPort.isCategoryExists(categoryId)).thenReturn(true);
        when(categoryPort.getCategoryById(categoryId)).thenReturn(category);

        assertThat(categoryService.getCategoryById(categoryId)).isEqualTo(category);
    }
}