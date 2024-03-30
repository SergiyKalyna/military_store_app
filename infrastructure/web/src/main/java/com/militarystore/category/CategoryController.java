package com.militarystore.category;

import com.militarystore.converter.category.CategoryConverter;
import com.militarystore.model.dto.category.CategoryDto;
import com.militarystore.model.dto.category.SubcategoryDto;
import com.militarystore.port.in.category.CategoryUseCase;
import com.militarystore.port.in.subcategory.SubcategoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryUseCase categoryUseCase;
    private final SubcategoryUseCase subcategoryUseCase;
    private final CategoryConverter categoryConverter;

    @GetMapping
    public List<CategoryDto> getCategories() {
        var categories = categoryUseCase.getCategories();

        return categories.stream()
                .map(categoryConverter::convertToCategoryDto)
                .toList();
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getCategory(@PathVariable ("categoryId") int categoryId) {
        var category = categoryUseCase.getCategoryById(categoryId);

        return categoryConverter.convertToCategoryDto(category);
    }

    @PostMapping
    public void addCategory(@RequestParam ("categoryName") String categoryName) {
        var category = categoryConverter.convertToCategory(categoryName);

        categoryUseCase.addCategory(category);
    }

    @PutMapping("/{categoryId}")
    public void updateCategory(@PathVariable ("categoryId") int categoryId, @RequestParam ("categoryName") String categoryName) {
        var category = categoryConverter.convertToCategory(categoryId, categoryName);

        categoryUseCase.updateCategory(category);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable ("categoryId") int categoryId) {
        categoryUseCase.deleteCategory(categoryId);
    }

    @GetMapping("/{categoryId}/subcategories")
    public List<SubcategoryDto> getSubcategoriesByCategoryId(@PathVariable ("categoryId") int categoryId) {
        var subcategories = subcategoryUseCase.getSubcategoriesByCategoryId(categoryId);

        return subcategories.stream()
                .map(categoryConverter::convertToSubcategoryDto)
                .toList();
    }

    @PostMapping("/{categoryId}/subcategories")
    public void addSubcategory(
        @PathVariable ("categoryId") int categoryId,
        @RequestParam ("subcategoryName") String subcategoryName
    ) {
        var subcategory = categoryConverter.convertToSubcategory(categoryId, subcategoryName);

        subcategoryUseCase.addSubcategory(subcategory);
    }

    @PutMapping("/{categoryId}/subcategories/{subcategoryId}")
    public void updateSubcategory(
        @PathVariable ("categoryId") int categoryId,
        @PathVariable ("subcategoryId") int subcategoryId,
        @RequestParam ("subcategoryName") String subcategoryName
    ) {
        var subcategory = categoryConverter.convertToSubcategory(categoryId, subcategoryId, subcategoryName);

        subcategoryUseCase.updateSubcategory(subcategory);
    }

    @DeleteMapping("/subcategories/{subcategoryId}")
    public void deleteSubcategory(@PathVariable ("subcategoryId") int subcategoryId) {
        subcategoryUseCase.deleteSubcategory(subcategoryId);
    }

    @GetMapping("/subcategories/{subcategoryId}")
    public SubcategoryDto getSubcategory(@PathVariable ("subcategoryId") int subcategoryId) {
        var subcategory = subcategoryUseCase.getSubcategoryById(subcategoryId);

        return categoryConverter.convertToSubcategoryDto(subcategory);
    }
}
