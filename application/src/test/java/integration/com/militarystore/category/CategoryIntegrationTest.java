package com.militarystore.category;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.category.Category;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.in.category.CategoryUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.militarystore.jooq.Tables.CATEGORIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryIntegrationTest extends IntegrationTest {

    @Autowired
    private CategoryUseCase categoryUseCase;

    @Test
    void getCategories() {
        assertThat(categoryUseCase.getCategories()).isEmpty();

        var category1 = Category.builder()
            .id(1)
            .name("category")
            .build();

        var category2 = Category.builder()
            .id(2)
            .name("category2")
            .build();

        createCategory(category1);
        createCategory(category2);

        assertThat(categoryUseCase.getCategories()).isEqualTo(List.of(category1, category2));
    }

    @Test
    void getCategoryById() {
        var category = Category.builder()
            .id(1)
            .name("category")
            .build();

        createCategory(category);

        assertThat(categoryUseCase.getCategoryById(1)).isEqualTo(category);
    }

    @Test
    void getCategoryById_whenCategoryNotFound_shouldTrowException() {
        assertThatThrownBy(() -> categoryUseCase.getCategoryById(1))
            .isInstanceOf(MsNotFoundException.class)
            .hasMessage("Category with id '1' does not exist");
    }

    @Test
    void createCategory() {
        var category = Category.builder()
            .name("category")
            .build();

        categoryUseCase.addCategory(category);

        assertThat(categoryUseCase.getCategories()).isNotEmpty();
    }

    @Test
    void updateCategory() {
        var category = Category.builder()
            .id(1)
            .name("category")
            .build();

        createCategory(category);

        var updatedCategory = Category.builder()
            .id(1)
            .name("updatedCategory")
            .build();

        categoryUseCase.updateCategory(updatedCategory);

        assertThat(categoryUseCase.getCategoryById(1)).isEqualTo(updatedCategory);
    }

    @Test
    void updateCategory_whenCategoryNotFound_shouldThrowException() {
        var category = Category.builder()
            .id(1)
            .name("category")
            .build();

        assertThatThrownBy(() -> categoryUseCase.updateCategory(category))
            .isInstanceOf(MsNotFoundException.class)
            .hasMessage("Category with id '1' does not exist");
    }

    @Test
    void deleteCategory() {
        var category = Category.builder()
            .id(1)
            .name("category")
            .build();

        createCategory(category);

        categoryUseCase.deleteCategory(1);

        assertThat(categoryUseCase.getCategories()).isEmpty();
    }

    @Test
    void deleteCategory_whenCategoryNotFound_shouldThrowException() {
        assertThatThrownBy(() -> categoryUseCase.deleteCategory(1))
            .isInstanceOf(MsNotFoundException.class)
            .hasMessage("Category with id '1' does not exist");
    }

    private void createCategory(Category category) {
       dslContext.insertInto(CATEGORIES)
           .set(CATEGORIES.ID, category.id())
           .set(CATEGORIES.NAME, category.name())
           .execute();
    }
}
