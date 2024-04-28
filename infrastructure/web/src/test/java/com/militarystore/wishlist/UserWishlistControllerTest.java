package com.militarystore.wishlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.militarystore.converter.product.ProductConverter;
import com.militarystore.entity.product.Product;
import com.militarystore.model.dto.product.ProductDto;
import com.militarystore.port.in.wishlist.UserWishlistUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserWishlistController.class)
@ContextConfiguration(classes = {UserWishlistController.class})
class UserWishlistControllerTest {

    private static final Integer PRODUCT_ID = 1;
    private static final Integer USER_ID = 11;

    @MockBean
    private UserWishlistUseCase userWishlistUseCase;

    @MockBean
    private ProductConverter productConverter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addProductToUserWishlist() throws Exception {
        mockMvc.perform(post("/users/wishlist/product/{productId}/user/{userId}", PRODUCT_ID, USER_ID))
            .andExpect(status().isOk());
    }

    @Test
    void deleteProductFromUserWishlist() throws Exception {
        mockMvc.perform(delete("/users/wishlist/product/{productId}/user/{userId}", PRODUCT_ID, USER_ID))
            .andExpect(status().isOk());
    }

    @Test
    void deleteAllUserProductsFromWishlist() throws Exception {
        mockMvc.perform(delete("/users/wishlist/user/{userId}", USER_ID))
            .andExpect(status().isOk());
    }

    @Test
    void getUserWishlistProducts() throws Exception {
        var product = Product.builder().build();
        var productDto = ProductDto.builder().build();

        when(userWishlistUseCase.getUserWishlistProducts(USER_ID)).thenReturn(List.of(product));
        when(productConverter.convertToSearchProductDto(product)).thenReturn(productDto);

        mockMvc.perform(get("/users/wishlist/user/{userId}", USER_ID))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(List.of(productDto))));
    }
}