package com.militarystore.category;

import com.militarystore.category.mapper.CategoryMapper;
import com.militarystore.entity.category.Category;
import com.militarystore.port.out.category.CategoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryAdapter implements CategoryPort {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public void addCategory(Category category) {
        categoryRepository.addCategory(category);
    }

    public void updateCategory(Category category) {
        categoryRepository.updateCategory(category);
    }

    public void deleteCategory(Integer id) {
        categoryRepository.deleteCategory(id);
    }

    public List<Category> getCategories() {
        return categoryRepository.getCategories().stream()
            .map(categoryMapper::map)
            .toList();
    }

    public boolean isCategoryExists(Integer id) {
        return categoryRepository.isCategoryExists(id);
    }

    public Category getCategoryById(Integer id) {
        return categoryMapper.map(categoryRepository.getCategoryById(id));
    }
}
