package com.militarystore.delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.militarystore.converter.delivery.DeliveryDetailsConverter;
import com.militarystore.entity.delivery.DeliveryDetails;
import com.militarystore.model.dto.delivery.DeliveryDetailsDto;
import com.militarystore.port.in.delivery.UpdateDeliveryDetailsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UpdateDeliveryDetailsController.class)
@ContextConfiguration(classes = UpdateDeliveryDetailsController.class)
class UpdateDeliveryDetailsControllerTest {

    @MockBean
    private UpdateDeliveryDetailsUseCase updateDeliveryDetailsUseCase;

    @MockBean
    private DeliveryDetailsConverter deliveryDetailsConverter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void updateDeliveryDetails() throws Exception {
        var deliveryDetailsDto = DeliveryDetailsDto.builder()
                .id(1)
                .city("city")
                .postNumber(1)
                .recipientName("recipientName")
                .recipientPhone("recipientPhone")
                .build();
        var deliveryDetails = DeliveryDetails.builder().build();

        when(deliveryDetailsConverter.convertToDeliveryDetails(deliveryDetailsDto)).thenReturn(deliveryDetails);

        mockMvc.perform(put("/orders/delivery-details")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(deliveryDetailsDto)))
                .andExpect(status().isOk());

        verify(updateDeliveryDetailsUseCase).updateDeliveryDetails(deliveryDetails);
    }
}