package com.militarystore.port.out.category;

import com.militarystore.entity.category.Category;

import java.util.List;

public interface CategoryPort {

    void addCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(Integer id);

    List<Category> getCategories();

    boolean isCategoryExists(Integer id);

    Category getCategoryById(Integer id);
}
