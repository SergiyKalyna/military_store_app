package com.militarystore.discount;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.militarystore.config.TestSecurityConfig;
import com.militarystore.converter.discount.DiscountConverter;
import com.militarystore.entity.discount.Discount;
import com.militarystore.model.dto.discount.DiscountDto;
import com.militarystore.port.in.discount.DiscountUseCase;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserDiscountController.class)
@ContextConfiguration(classes = {UserDiscountController.class})
@Import(TestSecurityConfig.class)
@WithMockUser
class UserDiscountControllerTest {

    private static final int USER_ID = 1;
    private static final String DISCOUNT_CODE = "discountCode";

    @MockBean
    private DiscountUseCase discountUseCase;

    @MockBean
    private DiscountConverter discountConverter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUserDiscountCode() throws Exception {
        when(discountUseCase.createUserDiscountCode(USER_ID)).thenReturn(DISCOUNT_CODE);

        mockMvc.perform(post("/users/discount/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(DISCOUNT_CODE));
    }

    @Test
    void getUserDiscounts() throws Exception {
        var discountDto = DiscountDto.builder().build();
        var discount = Discount.builder().build();

        when(discountUseCase.getUserDiscounts(USER_ID)).thenReturn(List.of(discount));
        when(discountConverter.toDto(discount)).thenReturn(discountDto);

        mockMvc.perform(get("/users/discount/1"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(List.of(discountDto))));
    }

    @Test
    void getUserDiscountByCode() throws Exception {
        var discount = 0.03;

        when(discountUseCase.getUserDiscountByCode(DISCOUNT_CODE, USER_ID)).thenReturn(discount);

        mockMvc.perform(get("/users/discount/1/discount-code/discountCode"))
            .andExpect(status().isOk())
            .andExpect(content().string(String.valueOf(discount)));
    }
}