package com.militarystore.port.in.subcategory;

import com.militarystore.entity.subcategory.Subcategory;

import java.util.List;

public interface SubcategoryUseCase {

    void addSubcategory(Subcategory subcategory);

    void updateSubcategory(Subcategory subcategory);

    void deleteSubcategory(Integer subcategoryId);

    List<Subcategory> getSubcategoriesByCategoryId(Integer categoryId);

    Subcategory getSubcategoryById(Integer subcategoryId);
}
