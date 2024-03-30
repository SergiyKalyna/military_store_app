package com.militarystore.port.in.subcategory;

import com.militarystore.entity.subcategory.Subcategory;

import java.util.List;

public interface SubcategoryUseCase {

    void addSubcategory(Subcategory subcategory);

    void updateSubcategory(Subcategory subcategory);

    void deleteSubcategory(int subcategoryId);

    List<Subcategory> getSubcategoriesByCategoryId(int categoryId);

    Subcategory getSubcategoryById(int subcategoryId);
}
