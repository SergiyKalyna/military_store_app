package com.militarystore.port.out.category;

import com.militarystore.entity.category.Category;

import java.util.List;

public interface CategoryPort {

    void addCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(int id);

    List<Category> getCategories();
}
