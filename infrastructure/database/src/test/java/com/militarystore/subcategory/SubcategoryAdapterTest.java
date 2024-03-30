package com.militarystore.subcategory;

import com.militarystore.entity.subcategory.Subcategory;
import com.militarystore.jooq.tables.records.SubcategoriesRecord;
import com.militarystore.subcategory.mapper.SubcategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubcategoryAdapterTest {

    @Mock
    private SubcategoryRepository subcategoryRepository;

    @Mock
    private SubcategoryMapper subcategoryMapper;

    private SubcategoryAdapter subcategoryAdapter;

    @BeforeEach
    void setUp() {
        subcategoryAdapter = new SubcategoryAdapter(subcategoryRepository, subcategoryMapper);
    }

    @Test
    void addSubcategory() {
        var subcategory = Subcategory.builder().build();

        subcategoryAdapter.addSubcategory(subcategory);

        verify(subcategoryRepository).addSubcategory(subcategory);
    }

    @Test
    void updateSubcategory() {
        var subcategory = Subcategory.builder().build();

        subcategoryAdapter.updateSubcategory(subcategory);

        verify(subcategoryRepository).updateSubcategory(subcategory);
    }

    @Test
    void deleteSubcategory() {
        subcategoryAdapter.deleteSubcategory(1);

        verify(subcategoryRepository).deleteSubcategory(1);
    }

    @Test
    void getSubcategoriesByCategoryId() {
        var subcategoryRecord = new SubcategoriesRecord();
        var subcategory = Subcategory.builder().build();

        when(subcategoryRepository.getSubcategoriesByCategoryId(1)).thenReturn(List.of(subcategoryRecord));
        when(subcategoryMapper.map(subcategoryRecord)).thenReturn(subcategory);

        assertThat(subcategoryAdapter.getSubcategoriesByCategoryId(1)).isEqualTo(List.of(subcategory));
    }

    @Test
    void isSubcategoryExists() {
        when(subcategoryRepository.isSubcategoryExists(1)).thenReturn(true);

        assertThat(subcategoryAdapter.isSubcategoryExists(1)).isTrue();
    }

    @Test
    void getSubcategoryById() {
        var subcategoryRecord = new SubcategoriesRecord();
        var subcategory = Subcategory.builder().build();

        when(subcategoryRepository.getSubcategoryById(1)).thenReturn(subcategoryRecord);
        when(subcategoryMapper.map(subcategoryRecord)).thenReturn(subcategory);

        assertThat(subcategoryAdapter.getSubcategoryById(1)).isEqualTo(subcategory);
    }
}