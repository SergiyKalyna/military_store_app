package com.militarystore.converter.delivery;

import com.militarystore.entity.delivery.DeliveryDetails;
import com.militarystore.model.dto.delivery.DeliveryDetailsDto;
import org.springframework.stereotype.Component;

@Component
public class DeliveryDetailsConverter {

    public DeliveryDetailsDto convertToDto(DeliveryDetails deliveryDetails) {
        return DeliveryDetailsDto.builder()
                .id(deliveryDetails.id())
                .city(deliveryDetails.city())
                .postNumber(deliveryDetails.postNumber())
                .recipientName(deliveryDetails.recipientName())
                .recipientPhone(deliveryDetails.recipientPhone())
                .build();
    }

    public DeliveryDetails convertToDeliveryDetails(DeliveryDetailsDto deliveryDetailsDto) {
        return DeliveryDetails.builder()
                .id(deliveryDetailsDto.id())
                .city(deliveryDetailsDto.city())
                .postNumber(deliveryDetailsDto.postNumber())
                .recipientName(deliveryDetailsDto.recipientName())
                .recipientPhone(deliveryDetailsDto.recipientPhone())
                .build();
    }
}
