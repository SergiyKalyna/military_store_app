package com.militarystore.wishlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.militarystore.config.TestSecurityConfig;
import com.militarystore.converter.product.ProductConverter;
import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.ProductDetails;
import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Role;
import com.militarystore.model.dto.product.ProductDto;
import com.militarystore.port.in.wishlist.UserWishlistUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserWishlistController.class)
@ContextConfiguration(classes = {UserWishlistController.class})
@Import(TestSecurityConfig.class)
@WithMockUser
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
        mockMvc.perform(post("/users/wishlist/product/{productId}", PRODUCT_ID)
                .with(user(User.builder().id(USER_ID).role(Role.USER).build())))
            .andExpect(status().isOk());
    }

    @Test
    void deleteProductFromUserWishlist() throws Exception {
        mockMvc.perform(delete("/users/wishlist/product/{productId}", PRODUCT_ID)
                .with(user(User.builder().id(USER_ID).role(Role.USER).build())))
            .andExpect(status().isOk());
    }

    @Test
    void deleteAllUserProductsFromWishlist() throws Exception {
        mockMvc.perform(delete("/users/wishlist")
                .with(user(User.builder().id(USER_ID).role(Role.USER).build())))
            .andExpect(status().isOk());
    }

    @Test
    void getUserWishlistProducts() throws Exception {
        var images = List.of(new byte[0]);
        var product = Product.builder().build();
        var productDetails = ProductDetails.builder().product(product).images(images).build();
        var productDto = ProductDto.builder().images(images).build();

        when(userWishlistUseCase.getUserWishlistProducts(USER_ID)).thenReturn(List.of(productDetails));
        when(productConverter.convertToSearchProductDto(productDetails)).thenReturn(productDto);

        mockMvc.perform(get("/users/wishlist")
                .with(user(User.builder().id(USER_ID).role(Role.USER).build())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(List.of(productDto))));
    }
}