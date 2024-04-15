package com.militarystore.product;

import com.militarystore.port.in.product.ProductRateUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRateController.class)
@ContextConfiguration(classes = {ProductRateController.class})
class ProductRateControllerTest {

    @MockBean
    private ProductRateUseCase productRateUseCase;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void rateProduct() throws Exception {
        var productRate = 4.5;
        var userId = 1;

        mockMvc.perform(post("/products/rate/1")
                .param("userId", String.valueOf(userId))
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