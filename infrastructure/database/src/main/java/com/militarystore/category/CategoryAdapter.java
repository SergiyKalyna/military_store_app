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

    public void deleteCategory(int id) {
        categoryRepository.deleteCategory(id);
    }

    public List<Category> getCategories() {
        return categoryRepository.getCategories().stream()
            .map(categoryMapper::map)
            .toList();
    }
}
