package com.militarystore.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.militarystore.config.TestSecurityConfig;
import com.militarystore.converter.product.ProductConverter;
import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.ProductDetails;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Role;
import com.militarystore.model.dto.product.ProductDto;
import com.militarystore.model.dto.product.ProductSizeGridTypeDto;
import com.militarystore.model.dto.product.ProductTagDto;
import com.militarystore.model.request.product.ProductRequest;
import com.militarystore.port.in.product.ProductUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = {ProductController.class})
@Import(TestSecurityConfig.class)
class ProductControllerTest {

    @MockBean
    private ProductUseCase productUseCase;

    @MockBean
    private ProductConverter productConverter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void addProduct_whenUserHasRoleAdmin_shouldAddProduct() throws Exception {
        var productRequest = new ProductRequest(
            "Product",
            "Product description",
            100,
            1,
            ProductSizeGridTypeDto.CLOTHES,
            ProductTagDto.NEW,
            List.of()
        );
        var product = Product.builder()
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridType.CLOTHES)
            .tag(ProductTag.NEW)
            .stockDetails(List.of())
            .build();
        var mockMultipartFile = new MockMultipartFile("images", "test.jpg", "image/jpeg", "test image content".getBytes());
        var requestJson = new MockMultipartFile("request", "", "application/json", objectMapper.writeValueAsString(productRequest).getBytes());

        when(productConverter.convertToProduct(productRequest)).thenReturn(product);
        when(productUseCase.addProduct(product, List.of(mockMultipartFile))).thenReturn(1);

        mockMvc.perform(multipart("/products")
                .file(mockMultipartFile)
                .file(requestJson)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().string("1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void addProduct_whenUserHasRoleUser_shouldReturnForbidden() throws Exception {
        var productRequest = new ProductRequest(
            "Product",
            "Product description",
            100,
            1,
            ProductSizeGridTypeDto.CLOTHES,
            ProductTagDto.NEW,
            List.of()
        );
        var mockMultipartFile = new MockMultipartFile("images", "test.jpg", "image/jpeg", "test image content".getBytes());
        var requestJson = new MockMultipartFile("request", "", "application/json", objectMapper.writeValueAsString(productRequest).getBytes());

        mockMvc.perform(multipart("/products")
                .file(mockMultipartFile)
                .file(requestJson)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void addProduct_whenUserHasRoleSuperAdmin_shouldAddProduct() throws Exception {
        var productRequest = new ProductRequest(
            "Product",
            "Product description",
            100,
            1,
            ProductSizeGridTypeDto.CLOTHES,
            ProductTagDto.NEW,
            List.of()
        );
        var product = Product.builder()
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridType.CLOTHES)
            .tag(ProductTag.NEW)
            .stockDetails(List.of())
            .build();
        var mockMultipartFile = new MockMultipartFile("images", "test.jpg", "image/jpeg", "test image content".getBytes());
        var requestJson = new MockMultipartFile("request", "", "application/json", objectMapper.writeValueAsString(productRequest).getBytes());

        when(productConverter.convertToProduct(productRequest)).thenReturn(product);
        when(productUseCase.addProduct(product, List.of(mockMultipartFile))).thenReturn(1);

        mockMvc.perform(multipart("/products")
                .file(mockMultipartFile)
                .file(requestJson)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().string("1"));
    }

    @Test
    void getProductById_whenUserUnauthorized_shouldUseUnauthorizedUserId() throws Exception {
        var product = Product.builder()
            .id(1)
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridType.CLOTHES)
            .tag(ProductTag.NEW)
            .stockDetails(List.of())
            .build();
        var image = new byte[0];
        var productDetails = ProductDetails.builder()
            .product(product)
            .images(List.of(image))
            .build();

        var productDto = ProductDto.builder()
            .id(1)
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridTypeDto.CLOTHES)
            .tag(ProductTagDto.NEW)
            .stockDetails(List.of())
            .images(List.of(image))
            .build();

        when(productUseCase.getProductById(1, 0)).thenReturn(productDetails);
        when(productConverter.convertToProductDto(productDetails)).thenReturn(productDto);

        mockMvc.perform(get("/products/1"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(productDto)));
    }

    @Test
    void getProductById_whenUserAuthorized_shouldUseUserId() throws Exception {
        var product = Product.builder()
            .id(1)
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridType.CLOTHES)
            .tag(ProductTag.NEW)
            .stockDetails(List.of())
            .build();
        var image = new byte[0];
        var productDetails = ProductDetails.builder()
            .product(product)
            .images(List.of(image))
            .build();

        var productDto = ProductDto.builder()
            .id(1)
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridTypeDto.CLOTHES)
            .tag(ProductTagDto.NEW)
            .stockDetails(List.of())
            .images(List.of(image))
            .build();

        when(productUseCase.getProductById(1, 1)).thenReturn(productDetails);
        when(productConverter.convertToProductDto(productDetails)).thenReturn(productDto);

        mockMvc.perform(get("/products/1")
                .with(user(User.builder().id(1).role(Role.USER).build())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(productDto)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProduct_whenUserHasRoleAdmin_shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/products/1"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteProduct_whenUserHasRoleUser_shouldReturnForbidden() throws Exception {
        mockMvc.perform(delete("/products/1"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void deleteProduct_whenUserHasRoleSuperAdmin_shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/products/1"))
            .andExpect(status().isOk());
    }

    @Test
    void getProductsBySubcategoryId() throws Exception {
        var product = Product.builder()
            .id(1)
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridType.CLOTHES)
            .tag(ProductTag.NEW)
            .stockDetails(List.of())
            .build();

        var image = new byte[0];
        var productDetails = ProductDetails.builder()
            .product(product)
            .images(List.of(image))
            .build();

        var productDto = ProductDto.builder()
            .id(1)
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridTypeDto.CLOTHES)
            .tag(ProductTagDto.NEW)
            .stockDetails(List.of())
            .images(List.of(image))
            .build();

        when(productUseCase.getProductsBySubcategoryId(1)).thenReturn(List.of(productDetails));
        when(productConverter.convertToSearchProductDto(productDetails)).thenReturn(productDto);

        mockMvc.perform(get("/products/subcategory-id/1"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(List.of(productDto))));
    }

    @Test
    void getProductsByName() throws Exception {
        var product = Product.builder()
            .id(1)
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridType.CLOTHES)
            .tag(ProductTag.NEW)
            .stockDetails(List.of())
            .build();

        var image = new byte[0];
        var productDetails = ProductDetails.builder()
            .product(product)
            .images(List.of(image))
            .build();

        var productDto = ProductDto.builder()
            .id(1)
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridTypeDto.CLOTHES)
            .tag(ProductTagDto.NEW)
            .stockDetails(List.of())
            .images(List.of(image))
            .build();

        when(productUseCase.getProductsByName("Product")).thenReturn(List.of(productDetails));
        when(productConverter.convertToSearchProductDto(productDetails)).thenReturn(productDto);

        mockMvc.perform(get("/products/product-name/Product"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(List.of(productDto))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProduct_whenUserHasRoleAdmin_shouldUpdateProduct() throws Exception {
        var productRequest = new ProductRequest(
            "Product",
            "Product description",
            100,
            1,
            ProductSizeGridTypeDto.CLOTHES,
            ProductTagDto.NEW,
            List.of()
        );
        var product = Product.builder()
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridType.CLOTHES)
            .tag(ProductTag.NEW)
            .stockDetails(List.of())
            .build();

        when(productConverter.convertToProduct(1, productRequest)).thenReturn(product);

        mockMvc.perform(put("/products/update/1")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(productRequest)))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateProduct_whenUserHasRoleUser_shouldReturnForbidden() throws Exception {
        var productRequest = new ProductRequest(
            "Product",
            "Product description",
            100,
            1,
            ProductSizeGridTypeDto.CLOTHES,
            ProductTagDto.NEW,
            List.of()
        );

        mockMvc.perform(put("/products/update/1")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(productRequest)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void updateProduct_whenUserHasRoleSuperAdmin_shouldUpdateProduct() throws Exception {
        var productRequest = new ProductRequest(
            "Product",
            "Product description",
            100,
            1,
            ProductSizeGridTypeDto.CLOTHES,
            ProductTagDto.NEW,
            List.of()
        );
        var product = Product.builder()
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridType.CLOTHES)
            .tag(ProductTag.NEW)
            .stockDetails(List.of())
            .build();

        when(productConverter.convertToProduct(1, productRequest)).thenReturn(product);

        mockMvc.perform(put("/products/update/1")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(productRequest)))
            .andExpect(status().isOk());
    }
}