package com.militarystore.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.militarystore.config.TestSecurityConfig;
import com.militarystore.converter.category.CategoryConverter;
import com.militarystore.entity.category.Category;
import com.militarystore.entity.subcategory.Subcategory;
import com.militarystore.model.dto.category.CategoryDto;
import com.militarystore.model.dto.category.SubcategoryDto;
import com.militarystore.port.in.category.CategoryUseCase;
import com.militarystore.port.in.subcategory.SubcategoryUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@ContextConfiguration(classes = {CategoryController.class})
@Import(TestSecurityConfig.class)
class CategoryControllerTest {

    @MockBean
    private CategoryUseCase categoryUseCase;

    @MockBean
    private CategoryConverter categoryConverter;

    @MockBean
    private SubcategoryUseCase subcategoryUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCategories() throws Exception {
        var category = Category.builder().id(1).name("category").build();
        var categories = List.of(category);

        var categoryDto = CategoryDto.builder().id(1).name("category").build();
        var categoriesDto = List.of(categoryDto);

        when(categoryUseCase.getCategories()).thenReturn(categories);
        when(categoryConverter.convertToCategoryDto(category)).thenReturn(categoryDto);

        mockMvc.perform(get("/categories"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(categoriesDto)));
    }

    @Test
    void getCategory() throws Exception {
        var category = Category.builder().id(1).name("category").build();
        var categoryDto = CategoryDto.builder().id(1).name("category").build();

        when(categoryUseCase.getCategoryById(1)).thenReturn(category);
        when(categoryConverter.convertToCategoryDto(category)).thenReturn(categoryDto);

        mockMvc.perform(get("/categories/1"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(categoryDto)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addCategory_whenUserHasRoleAdmin_shouldAddCategory() throws Exception {
        var category = Category.builder().name("category").build();

        doNothing().when(categoryUseCase).addCategory(category);

        mockMvc.perform(post("/categories")
                .param("categoryName", "category"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void addCategory_whenUserHasRoleSuperAdmin_shouldAddCategory() throws Exception {
        var category = Category.builder().name("category").build();

        doNothing().when(categoryUseCase).addCategory(category);

        mockMvc.perform(post("/categories")
                .param("categoryName", "category"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void addCategory_whenUserHasRoleUser_shouldReturnAccessDenied() throws Exception {
        var category = Category.builder().name("category").build();

        doNothing().when(categoryUseCase).addCategory(category);

        mockMvc.perform(post("/categories")
                .param("categoryName", "category"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_whenUserHasRoleAdmin_shouldUpdate() throws Exception {
        var category = Category.builder().id(1).name("category").build();

        doNothing().when(categoryUseCase).updateCategory(category);

        mockMvc.perform(put("/categories/1")
                .param("categoryName", "category"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void updateCategory_whenUserHasRoleSuperAdmin_shouldUpdate() throws Exception {
        var category = Category.builder().id(1).name("category").build();

        doNothing().when(categoryUseCase).updateCategory(category);

        mockMvc.perform(put("/categories/1")
                .param("categoryName", "category"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateCategory_whenUserHasRoleUser_shouldReturnAccessDenied() throws Exception {
        var category = Category.builder().id(1).name("category").build();

        doNothing().when(categoryUseCase).updateCategory(category);

        mockMvc.perform(put("/categories/1")
                .param("categoryName", "category"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_whenUserHasRoleAdmin_shouldDeleteCategory() throws Exception {
        doNothing().when(categoryUseCase).deleteCategory(1);

        mockMvc.perform(delete("/categories/1"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void deleteCategory_whenUserHasRoleSuperAdmin_shouldDeleteCategory() throws Exception {
        doNothing().when(categoryUseCase).deleteCategory(1);

        mockMvc.perform(delete("/categories/1"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteCategory_whenUserHasRoleUser_shouldReturnAccessDenied() throws Exception {
        doNothing().when(categoryUseCase).deleteCategory(1);

        mockMvc.perform(delete("/categories/1"))
            .andExpect(status().isForbidden());
    }

    @Test
    void getSubcategoriesByCategoryId() throws Exception {
        var subcategory = Subcategory.builder().id(1).name("subcategory").build();
        var subcategories = List.of(subcategory);

        var subcategoryDto = SubcategoryDto.builder().id(1).name("subcategory").build();
        var subcategoriesDto = List.of(subcategoryDto);

        when(subcategoryUseCase.getSubcategoriesByCategoryId(1)).thenReturn(subcategories);
        when(categoryConverter.convertToSubcategoryDto(subcategory)).thenReturn(subcategoryDto);

        mockMvc.perform(get("/categories/1/subcategories"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(subcategoriesDto)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addSubcategory_whenUserHasRoleAdmin_shouldAddCategory() throws Exception {
        var subcategory = Subcategory.builder().name("subcategory").categoryId(1).build();

        doNothing().when(subcategoryUseCase).addSubcategory(subcategory);

        mockMvc.perform(post("/categories/1/subcategories")
                .param("subcategoryName", "subcategory"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void addSubcategory_whenUserHasRoleSuperAdmin_shouldAddCategory() throws Exception {
        var subcategory = Subcategory.builder().name("subcategory").categoryId(1).build();

        doNothing().when(subcategoryUseCase).addSubcategory(subcategory);

        mockMvc.perform(post("/categories/1/subcategories")
                .param("subcategoryName", "subcategory"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void addSubcategory_whenUserHasRoleUser_shouldReturnAccessDenied() throws Exception {
        var subcategory = Subcategory.builder().name("subcategory").categoryId(1).build();

        doNothing().when(subcategoryUseCase).addSubcategory(subcategory);

        mockMvc.perform(post("/categories/1/subcategories")
                .param("subcategoryName", "subcategory"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateSubcategory_whenUserHasRoleAdmin_shouldUpdateSubcategory() throws Exception {
        var subcategory = Subcategory.builder().id(1).name("subcategory").categoryId(1).build();

        doNothing().when(subcategoryUseCase).updateSubcategory(subcategory);

        mockMvc.perform(put("/categories/1/subcategories/1")
                .param("subcategoryName", "subcategory"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void updateSubcategory_whenUserHasRoleSuperAdmin_shouldUpdateSubcategory() throws Exception {
        var subcategory = Subcategory.builder().id(1).name("subcategory").categoryId(1).build();

        doNothing().when(subcategoryUseCase).updateSubcategory(subcategory);

        mockMvc.perform(put("/categories/1/subcategories/1")
                .param("subcategoryName", "subcategory"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateSubcategory_whenUserHasRoleUser_shouldReturnAccessDenied() throws Exception {
        var subcategory = Subcategory.builder().id(1).name("subcategory").categoryId(1).build();

        doNothing().when(subcategoryUseCase).updateSubcategory(subcategory);

        mockMvc.perform(put("/categories/1/subcategories/1")
                .param("subcategoryName", "subcategory"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteSubcategory_whenUserHasRoleAdmin_shouldDeleteSubcategory() throws Exception {
        doNothing().when(subcategoryUseCase).deleteSubcategory(1);

        mockMvc.perform(delete("/categories/subcategories/1"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void deleteSubcategory_whenUserHasRoleSuperAdmin_shouldDeleteSubcategory() throws Exception {
        doNothing().when(subcategoryUseCase).deleteSubcategory(1);

        mockMvc.perform(delete("/categories/subcategories/1"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteSubcategory_whenUserHasRoleUser_shouldReturnAccessDenied() throws Exception {
        doNothing().when(subcategoryUseCase).deleteSubcategory(1);

        mockMvc.perform(delete("/categories/subcategories/1"))
            .andExpect(status().isForbidden());
    }

    @Test
    void getSubcategory() throws Exception {
        var subcategory = Subcategory.builder().id(1).name("subcategory").build();
        var subcategoryDto = SubcategoryDto.builder().id(1).name("subcategory").build();

        when(subcategoryUseCase.getSubcategoryById(1)).thenReturn(subcategory);
        when(categoryConverter.convertToSubcategoryDto(subcategory)).thenReturn(subcategoryDto);

        mockMvc.perform(get("/categories/subcategories/1"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(subcategoryDto)));
    }
}