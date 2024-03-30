package com.militarystore.subcategory;

import com.militarystore.entity.subcategory.Subcategory;
import com.militarystore.port.out.subcategory.SubcategoryPort;
import com.militarystore.subcategory.mapper.SubcategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubcategoryAdapter implements SubcategoryPort {

    private final SubcategoryRepository subcategoryRepository;
    private final SubcategoryMapper subcategoryMapper;

    public void addSubcategory(Subcategory subcategory) {
        subcategoryRepository.addSubcategory(subcategory);
    }

    public void updateSubcategory(Subcategory subcategory) {
        subcategoryRepository.updateSubcategory(subcategory);
    }

    public void deleteSubcategory(int id) {
        subcategoryRepository.deleteSubcategory(id);
    }

    public List<Subcategory> getSubcategoriesByCategoryId(int categoryId) {
        return subcategoryRepository.getSubcategoriesByCategoryId(categoryId).stream()
            .map(subcategoryMapper::map)
            .toList();
    }

    public boolean isSubcategoryExists(int id) {
        return subcategoryRepository.isSubcategoryExists(id);
    }
}
