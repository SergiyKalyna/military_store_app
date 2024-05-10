package com.militarystore.product;

import com.militarystore.config.TestSecurityConfig;
import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Role;
import com.militarystore.port.in.product.ProductRateUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRateController.class)
@ContextConfiguration(classes = {ProductRateController.class})
@Import(TestSecurityConfig.class)
@WithMockUser
class ProductRateControllerTest {

    @MockBean
    private ProductRateUseCase productRateUseCase;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void rateProduct() throws Exception {
        var productRate = 4.5;

        mockMvc.perform(post("/products/rate/1")
                .with(user(User.builder().id(1).role(Role.USER).build()))
                .param("productRate", String.valueOf(productRate)))
            .andExpect(status().isOk());
    }

    @Test
    void getAverageRateByProductId() throws Exception {
        var productRate = 4.5;
        var productId = 1;

        when(productRateUseCase.getAverageRateByProductId(productId)).thenReturn(productRate);

        mockMvc.perform(get("/products/rate/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(String.valueOf(productRate)));
    }
}