package com.militarystore.port.out.subcategory;

import com.militarystore.entity.subcategory.Subcategory;

import java.util.List;

public interface SubcategoryPort {

    void addSubcategory(Subcategory subcategory);

    void updateSubcategory(Subcategory subcategory);

    void deleteSubcategory(Integer id);

    List<Subcategory> getSubcategoriesByCategoryId(Integer categoryId);

    boolean isSubcategoryExists(Integer id);

    Subcategory getSubcategoryById(Integer id);
}
