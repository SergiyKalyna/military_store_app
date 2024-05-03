package com.militarystore.converter.delivery;

import com.militarystore.entity.delivery.DeliveryDetails;
import com.militarystore.model.dto.delivery.DeliveryDetailsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryDetailsConverterTest {

    private DeliveryDetailsConverter deliveryDetailsConverter;

    @BeforeEach
    void setUp() {
        deliveryDetailsConverter = new DeliveryDetailsConverter();
    }

    @Test
    void convertToDto() {
        var deliveryDetails = DeliveryDetails.builder()
                .id(1)
                .city("city")
                .postNumber(1)
                .recipientName("recipientName")
                .recipientPhone("recipientPhone")
                .build();

        var expected = DeliveryDetailsDto.builder()
                .id(1)
                .city("city")
                .postNumber(1)
                .recipientName("recipientName")
                .recipientPhone("recipientPhone")
                .build();

        assertThat(deliveryDetailsConverter.convertToDto(deliveryDetails)).isEqualTo(expected);
    }

    @Test
    void convertToDeliveryDetails() {
        var deliveryDetailsDto = DeliveryDetailsDto.builder()
                .id(1)
                .city("city")
                .postNumber(1)
                .recipientName("recipientName")
                .recipientPhone("recipientPhone")
                .build();

        var expected = DeliveryDetails.builder()
                .id(1)
                .city("city")
                .postNumber(1)
                .recipientName("recipientName")
                .recipientPhone("recipientPhone")
                .build();

        assertThat(deliveryDetailsConverter.convertToDeliveryDetails(deliveryDetailsDto)).isEqualTo(expected);
    }
}