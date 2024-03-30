package com.militarystore.subcategory;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.subcategory.Subcategory;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.in.subcategory.SubcategoryUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.militarystore.jooq.Tables.CATEGORIES;
import static com.militarystore.jooq.Tables.SUBCATEGORIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubcategoryIntegrationTest extends IntegrationTest {

    private static final int CATEGORY_ID = 1;

    @Autowired
    private SubcategoryUseCase subcategoryUseCase;

    @BeforeEach
    void setUp() {
        createCategory();
    }

    @Test
    void getSubcategories() {
        assertThat(subcategoryUseCase.getSubcategoriesByCategoryId(CATEGORY_ID)).isEmpty();

        var subcategory1 = Subcategory.builder()
            .id(1)
            .name("subcategory")
            .categoryId(CATEGORY_ID)
            .build();

        var subcategory2 = Subcategory.builder()
            .id(2)
            .name("subcategory2")
            .categoryId(CATEGORY_ID)
            .build();

        createSubcategory(subcategory1);
        createSubcategory(subcategory2);

        assertThat(subcategoryUseCase.getSubcategoriesByCategoryId(CATEGORY_ID)).isEqualTo(List.of(subcategory1, subcategory2));
    }

    @Test
    void getSubcategoryById() {
        var subcategory = Subcategory.builder()
            .id(1)
            .name("subcategory")
            .categoryId(CATEGORY_ID)
            .build();

        createSubcategory(subcategory);

        assertThat(subcategoryUseCase.getSubcategoryById(1)).isEqualTo(subcategory);
    }

    @Test
    void getSubcategoryById_whenSubcategoryNotFound_shouldTrowException() {
        assertThatThrownBy(() -> subcategoryUseCase.getSubcategoryById(1))
            .isInstanceOf(MsNotFoundException.class)
            .hasMessage("Subcategory with id '1' does not exist");
    }

    @Test
    void createSubcategory() {
        var subcategory = Subcategory.builder()
            .name("subcategory")
            .categoryId(CATEGORY_ID)
            .build();

        subcategoryUseCase.addSubcategory(subcategory);

        assertThat(subcategoryUseCase.getSubcategoriesByCategoryId(CATEGORY_ID)).isNotEmpty();
    }

    @Test
    void updateSubcategory() {
        var subcategory = Subcategory.builder()
            .id(1)
            .name("subcategory")
            .categoryId(CATEGORY_ID)
            .build();

        createSubcategory(subcategory);

        var subcategoryToUpdate = Subcategory.builder()
            .id(1)
            .name("subcategory2")
            .categoryId(CATEGORY_ID)
            .build();

        subcategoryUseCase.updateSubcategory(subcategoryToUpdate);

        assertThat(subcategoryUseCase.getSubcategoryById(1)).isEqualTo(subcategoryToUpdate);
    }

    @Test
    void updateSubcategory_whenSubcategoryIdDoesNotExist_shouldThrowException() {
        var subcategory = Subcategory.builder().id(1).name("subcategory").categoryId(2).build();

        assertThatThrownBy(() -> subcategoryUseCase.updateSubcategory(subcategory))
            .isInstanceOf(MsNotFoundException.class)
            .hasMessage("Subcategory with id '1' does not exist");
    }

    @Test
    void updateSubcategory_whenCategoryIdDoesNotExist_shouldThrowException() {
        var subcategory = Subcategory.builder().id(1).name("subcategory").categoryId(1).build();
        createSubcategory(subcategory);

        var subcategoryToUpdate = Subcategory.builder().id(1).name("subcategory2").categoryId(2).build();

        assertThatThrownBy(() -> subcategoryUseCase.updateSubcategory(subcategoryToUpdate))
            .isInstanceOf(MsNotFoundException.class)
            .hasMessage("Category with id '2' does not exist");
    }

    @Test
    void deleteSubcategory() {
        var subcategory = Subcategory.builder()
            .id(1)
            .name("subcategory")
            .categoryId(CATEGORY_ID)
            .build();

        createSubcategory(subcategory);

        subcategoryUseCase.deleteSubcategory(1);

        assertThat(subcategoryUseCase.getSubcategoriesByCategoryId(CATEGORY_ID)).isEmpty();
    }

    @Test
    void deleteSubcategory_whenSubcategoryDoesNotExist_shouldThrowException() {
        assertThatThrownBy(() -> subcategoryUseCase.deleteSubcategory(1))
            .isInstanceOf(MsNotFoundException.class)
            .hasMessage("Subcategory with id '1' does not exist");
    }

    private void createCategory() {
        dslContext.insertInto(CATEGORIES)
            .set(CATEGORIES.ID, CATEGORY_ID)
            .set(CATEGORIES.NAME, "test-category")
            .execute();
    }

    private void createSubcategory(Subcategory subcategory) {
        dslContext.insertInto(SUBCATEGORIES)
            .set(SUBCATEGORIES.ID, subcategory.id())
            .set(SUBCATEGORIES.NAME, subcategory.name())
            .set(SUBCATEGORIES.CATEGORY_ID, subcategory.categoryId())
            .execute();
    }
}
