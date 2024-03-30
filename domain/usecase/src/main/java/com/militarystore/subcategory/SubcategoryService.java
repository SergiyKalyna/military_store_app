package com.militarystore.subcategory;

import com.militarystore.entity.subcategory.Subcategory;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.in.subcategory.SubcategoryUseCase;
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
public class SubcategoryService implements SubcategoryUseCase {

    private final SubcategoryPort subcategoryPort;
    private final CategoryPort categoryPort;

    public void addSubcategory(Subcategory subcategory) {
        validateSubcategory(subcategory);

        subcategoryPort.addSubcategory(subcategory);
        log.info("Subcategory with name '{}' was added", subcategory);
    }

    public void updateSubcategory(Subcategory subcategory) {
        checkCategoriesExisting(subcategory.categoryId(), subcategory.id());
        validateSubcategory(subcategory);

        subcategoryPort.updateSubcategory(subcategory);
        log.info("Subcategory with name '{}' was updated", subcategory);
    }

    public void deleteSubcategory(int subcategoryId) {
        checkIfSubcategoryExists(subcategoryId);

        subcategoryPort.deleteSubcategory(subcategoryId);
        log.info("Subcategory with id '{}' was deleted", subcategoryId);
    }

    public List<Subcategory> getSubcategoriesByCategoryId(int categoryId) {
        return subcategoryPort.getSubcategoriesByCategoryId(categoryId);
    }

    public Subcategory getSubcategoryById(int subcategoryId) {
        checkIfSubcategoryExists(subcategoryId);

        return subcategoryPort.getSubcategoryById(subcategoryId);
    }

    private void validateSubcategory(Subcategory subcategory) {
        if (isNull(subcategory.name()) || subcategory.name().isBlank()) {
            throw new MsValidationException("Subcategory name should not be empty");
        }

        if (isNull(subcategory.categoryId())) {
            throw new MsValidationException("Subcategory should have a category id");
        }
    }

    private void checkIfSubcategoryExists(int subcategoryId) {
        if (!subcategoryPort.isSubcategoryExists(subcategoryId)) {
            throw new MsNotFoundException(String.format("Subcategory with id '%d' does not exist", subcategoryId));
        }
    }

    private void checkCategoriesExisting(int categoryId, int subcategoryId) {
        checkIfSubcategoryExists(subcategoryId);

        if (!categoryPort.isCategoryExists(categoryId)) {
            throw new MsNotFoundException(String.format("Category with id '%d' does not exist", categoryId));
        }
    }
}
