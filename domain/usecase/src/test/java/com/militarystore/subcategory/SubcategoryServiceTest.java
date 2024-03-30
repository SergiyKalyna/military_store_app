package com.militarystore.subcategory;

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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubcategoryServiceTest {

    @Mock
    private SubcategoryPort subcategoryPort;

    @Mock
    private CategoryPort categoryPort;

    private SubcategoryService subcategoryService;

    @BeforeEach
    void setUp() {
        subcategoryService = new SubcategoryService(subcategoryPort, categoryPort);
    }

    @Test
    void addSubcategory_whenSubcategoryNameIsNull_shouldThrowValidationException() {
        var subcategory = Subcategory.builder().build();

        assertThrows(MsValidationException.class, () -> subcategoryService.addSubcategory(subcategory));
    }

    @Test
    void addSubcategory_whenSubcategoryNameIsEmpty_shouldThrowValidationException() {
        var subcategory = Subcategory.builder().name("  ").build();

        assertThrows(MsValidationException.class, () -> subcategoryService.addSubcategory(subcategory));
    }

    @Test
    void addSubcategory_whenCategoryIdIsNull_shouldThrowValidationException() {
        var subcategory = Subcategory.builder().name("subcategory").build();

        assertThrows(MsValidationException.class, () -> subcategoryService.addSubcategory(subcategory));
    }

    @Test
    void addSubcategory_whenSubcategoryIsValid_shouldAddSubcategory() {
        var subcategory = Subcategory.builder().name("subcategory").categoryId(1).build();

        subcategoryService.addSubcategory(subcategory);

        verify(subcategoryPort).addSubcategory(subcategory);
    }

    @Test
    void updateSubcategory_whenSubcategoryDoesNotExist_shouldThrowNotFoundException() {
        var subcategory = Subcategory.builder().id(1).name("subcategory").categoryId(2).build();

        when(subcategoryPort.isSubcategoryExists(1)).thenReturn(false);

        assertThrows(MsNotFoundException.class, () -> subcategoryService.updateSubcategory(subcategory));
    }

    @Test
    void updateSubcategory_whenSubcategoryNameIsNull_shouldThrowValidationException() {
        var subcategory = Subcategory.builder().id(1).build();

        when(subcategoryPort.isSubcategoryExists(1)).thenReturn(true);

        assertThrows(MsValidationException.class, () -> subcategoryService.updateSubcategory(subcategory));
    }

    @Test
    void updateSubcategory_whenSubcategoryNameIsEmpty_shouldThrowValidationException() {
        var subcategory = Subcategory.builder().id(1).name("  ").build();

        when(subcategoryPort.isSubcategoryExists(1)).thenReturn(true);

        assertThrows(MsValidationException.class, () -> subcategoryService.updateSubcategory(subcategory));
    }

    @Test
    void updateSubcategory_whenCategoryIdIsNull_shouldThrowValidationException() {
        var subcategory = Subcategory.builder().id(1).name("subcategory").build();

        when(subcategoryPort.isSubcategoryExists(1)).thenReturn(true);

        assertThrows(MsValidationException.class, () -> subcategoryService.updateSubcategory(subcategory));
    }

    @Test
    void updateSubcategory_whenSubcategoryIsValid_shouldUpdateSubcategory() {
        var subcategory = Subcategory.builder().id(2).name("subcategory").categoryId(1).build();

        when(subcategoryPort.isSubcategoryExists(2)).thenReturn(true);

        subcategoryService.updateSubcategory(subcategory);

        verify(subcategoryPort).updateSubcategory(subcategory);
    }

    @Test
    void deleteSubcategory_whenSubcategoryDoesNotExist_shouldThrowNotFoundException() {
        var subcategoryId = 1;

        when(subcategoryPort.isSubcategoryExists(subcategoryId)).thenReturn(false);

        assertThrows(MsNotFoundException.class, () -> subcategoryService.deleteSubcategory(subcategoryId));
    }

    @Test
    void deleteSubcategory_whenSubcategoryIsValid_shouldDeleteSubcategory() {
        var subcategoryId = 1;

        when(subcategoryPort.isSubcategoryExists(subcategoryId)).thenReturn(true);

        subcategoryService.deleteSubcategory(subcategoryId);

        verify(subcategoryPort).deleteSubcategory(subcategoryId);
    }

    @Test
    void getSubcategoriesByCategoryId() {
        var categoryId = 1;
        var subcategories = List.of(Subcategory.builder().build());

        when(subcategoryPort.getSubcategoriesByCategoryId(categoryId)).thenReturn(subcategories);

        assertThat(subcategoryService.getSubcategoriesByCategoryId(categoryId)).isEqualTo(subcategories);
    }
}