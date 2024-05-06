package com.militarystore.model.request.order;

import com.militarystore.model.dto.delivery.DeliveryDetailsDto;
import com.militarystore.model.dto.order.OrderDetailsDto;
import lombok.Builder;

import java.util.List;

@Builder
public record SubmitOrderRequest(
    DeliveryDetailsDto deliveryDetailsDto,
    List<OrderDetailsDto> orderDetailsDto,
    Double discount,
    String discountCode,
    Integer totalAmount
) {
}
