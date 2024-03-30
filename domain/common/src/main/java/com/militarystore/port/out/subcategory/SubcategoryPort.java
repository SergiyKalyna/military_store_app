package com.militarystore.port.out.subcategory;

import com.militarystore.entity.subcategory.Subcategory;

import java.util.List;

public interface SubcategoryPort {

    void addSubcategory(Subcategory subcategory);

    void updateSubcategory(Subcategory subcategory);

    void deleteSubcategory(int id);

    List<Subcategory> getSubcategoriesByCategoryId(int categoryId);

    boolean isSubcategoryExists(int id);
}
