package com.militarystore.port.in.category;

import com.militarystore.entity.category.Category;

import java.util.List;

public interface CategoryUseCase {

    void addCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(int id);

    List<Category> getCategories();
}
