package com.militarystore.basket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.militarystore.config.TestSecurityConfig;
import com.militarystore.converter.basket.ProductBasketConverter;
import com.militarystore.entity.basket.ProductInBasket;
import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Role;
import com.militarystore.model.dto.basket.ProductInBasketDto;
import com.militarystore.model.response.basket.ProductBasketResponse;
import com.militarystore.port.in.basket.ProductBasketUseCase;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductBasketController.class)
@ContextConfiguration(classes = ProductBasketController.class)
@Import(TestSecurityConfig.class)
@WithMockUser
class ProductBasketControllerTest {

    private static final Integer PRODUCT_DETAILS_ID = 1;
    private static final Integer USER_ID = 1;

    @MockBean
    private ProductBasketUseCase productBasketUseCase;

    @MockBean
    private ProductBasketConverter productBasketConverter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addProductToBasket() throws Exception {
        mockMvc.perform(
                post("/products/basket/product-details-id/{product-details-id}", PRODUCT_DETAILS_ID)
                    .with(user(User.builder().id(USER_ID).role(Role.USER).build()))
                    .param("quantity", "1"))
            .andExpect(status().isOk());
    }

    @Test
    void updateProductQuantityInBasket() throws Exception {
        mockMvc.perform(
                put("/products/basket/product-details-id/{product-details-id}", PRODUCT_DETAILS_ID)
                    .with(user(User.builder().id(USER_ID).role(Role.USER).build()))
                    .param("quantity", "1"))
            .andExpect(status().isOk());
    }

    @Test
    void deleteProductFromBasket() throws Exception {
        mockMvc.perform(
                delete("/products/basket/product-details-id/{product-details-id}", PRODUCT_DETAILS_ID)
                    .with(user(User.builder().id(USER_ID).role(Role.USER).build()))
                    .param("quantity", "1"))
            .andExpect(status().isOk());
    }

    @Test
    void deleteUserProductsFromBasket() throws Exception {
        mockMvc.perform(delete("/products/basket")
                .with(user(User.builder().id(USER_ID).role(Role.USER).build())))
            .andExpect(status().isOk());
    }

    @Test
    void getUserBasketProducts() throws Exception {
        var productsInBasket = List.of(ProductInBasket.builder().build());
        var productBasketResponse = new ProductBasketResponse(List.of(ProductInBasketDto.builder().build()), 11);

        when(productBasketUseCase.getUserBasketProducts(USER_ID)).thenReturn(productsInBasket);
        when(productBasketConverter.convertToProductBasketResponse(productsInBasket)).thenReturn(productBasketResponse);

        mockMvc.perform(get("/products/basket")
                .with(user(User.builder().id(USER_ID).role(Role.USER).build())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(productBasketResponse)));
    }
}