package com.militarystore.category;

import com.militarystore.entity.category.Category;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.in.category.CategoryUseCase;
import com.militarystore.port.out.category.CategoryPort;
import com.militarystore.port.out.subcategory.SubcategoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService implements CategoryUseCase {

    private final CategoryPort categoryPort;
    private final SubcategoryPort subcategoryPort;

    public void addCategory(Category category) {
        validateCategory(category);

        categoryPort.addCategory(category);
        log.info("Category with name '{}' was added", category);
    }

    public void updateCategory(Category category) {
        checkIfCategoryExists(category.id());
        validateCategory(category);

        categoryPort.updateCategory(category);
        log.info("Category with name '{}' was updated", category);
    }

    public void deleteCategory(Integer categoryId) {
        verifyPossibilityToDeleteCategory(categoryId);

        categoryPort.deleteCategory(categoryId);
        log.info("Category with id '{}' was deleted", categoryId);
    }

    public List<Category> getCategories() {
        return categoryPort.getCategories();
    }

    public Category getCategoryById(Integer categoryId) {
        checkIfCategoryExists(categoryId);

        return categoryPort.getCategoryById(categoryId);
    }

    private void checkIfCategoryExists(Integer categoryId) {
        if (!categoryPort.isCategoryExists(categoryId)) {
            throw new MsNotFoundException(String.format("Category with id '%d' does not exist", categoryId));
        }
    }

    private void validateCategory(Category category) {
        if (isNull(category.name()) || category.name().isBlank()) {
            throw new MsValidationException("Category name cannot be empty");
        }
    }

    private void verifyPossibilityToDeleteCategory(Integer categoryId) {
        checkIfCategoryExists(categoryId);

        var subcategoriesOfCategory = subcategoryPort.getSubcategoriesByCategoryId(categoryId);
        if (!subcategoriesOfCategory.isEmpty()) {
            throw new MsValidationException("Category with id '" + categoryId + "' cannot be deleted because it has subcategories");
        }
    }
}
