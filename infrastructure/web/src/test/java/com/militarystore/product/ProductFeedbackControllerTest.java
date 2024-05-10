package com.militarystore.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.militarystore.config.TestSecurityConfig;
import com.militarystore.converter.product.ProductFeedbackConverter;
import com.militarystore.entity.product.ProductFeedback;
import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Role;
import com.militarystore.model.dto.product.ProductFeedbackDto;
import com.militarystore.model.request.product.ProductFeedbackRequest;
import com.militarystore.port.in.product.ProductFeedbackUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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

@WebMvcTest(ProductFeedbackController.class)
@ContextConfiguration(classes = ProductFeedbackController.class)
@Import(TestSecurityConfig.class)
@WithMockUser
class ProductFeedbackControllerTest {

    private static final Integer USER_ID = 1;
    private static final Integer FEEDBACK_ID = 10;
    private static final Integer PRODUCT_ID = 11;
    private static final String FEEDBACK = "Good product";

    @MockBean
    private ProductFeedbackUseCase productFeedbackUseCase;

    @MockBean
    private ProductFeedbackConverter productFeedbackConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void saveFeedback() throws Exception {
        var productFeedbackRequest = new ProductFeedbackRequest(PRODUCT_ID, FEEDBACK);
        var productFeedback = ProductFeedback.builder().build();

        when(productFeedbackConverter.convertToProductFeedback(productFeedbackRequest, USER_ID))
            .thenReturn(productFeedback);
        when(productFeedbackUseCase.saveFeedback(productFeedback)).thenReturn(1);

        mockMvc.perform(post("/products/feedback")
                .with(user(User.builder().id(1).role(Role.USER).build()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productFeedbackRequest)))
            .andExpect(status().isOk())
            .andExpect(content().string("1"));
    }

    @Test
    void updateFeedback() throws Exception {
        var productFeedbackRequest = new ProductFeedbackRequest(PRODUCT_ID, FEEDBACK);
        var productFeedback = ProductFeedback.builder().build();

        when(productFeedbackConverter.convertToProductFeedback(productFeedbackRequest, FEEDBACK_ID, USER_ID))
            .thenReturn(productFeedback);

        mockMvc.perform(put("/products/feedback/{feedbackId}", FEEDBACK_ID)
                .with(user(User.builder().id(1).role(Role.USER).build()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productFeedbackRequest)))
            .andExpect(status().isOk());
    }

    @Test
    void deleteFeedback() throws Exception {
        mockMvc.perform(delete("/products/feedback/{feedbackId}", FEEDBACK_ID)
                .with(user(User.builder().id(1).role(Role.USER).build()))
                .param("userId", USER_ID.toString()))
            .andExpect(status().isOk());
    }

    @Test
    void getFeedbackById() throws Exception {
        var feedback = ProductFeedback.builder().build();
        var productFeedbackDto = ProductFeedbackDto.builder().build();

        when(productFeedbackUseCase.getFeedbackById(FEEDBACK_ID)).thenReturn(feedback);
        when(productFeedbackConverter.convertToProductFeedbackDto(feedback)).thenReturn(productFeedbackDto);

        mockMvc.perform(get("/products/feedback/{feedbackId}", FEEDBACK_ID))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(productFeedbackDto)));
    }

    @Test
    void getFeedbacksByProductId() throws Exception {
        var feedback = ProductFeedback.builder().build();
        var productFeedbackDto = ProductFeedbackDto.builder().build();

        when(productFeedbackUseCase.getFeedbacksByProductId(PRODUCT_ID)).thenReturn(List.of(feedback));
        when(productFeedbackConverter.convertToProductFeedbackDto(feedback)).thenReturn(productFeedbackDto);

        mockMvc.perform(get("/products/feedback/product/{productId}", PRODUCT_ID))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(List.of(productFeedbackDto))));
    }
}