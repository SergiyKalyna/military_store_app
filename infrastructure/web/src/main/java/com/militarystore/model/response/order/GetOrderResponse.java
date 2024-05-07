package com.militarystore.model.response.order;

import com.militarystore.model.dto.delivery.DeliveryDetailsDto;
import com.militarystore.model.dto.order.OrderDetailsDto;
import com.militarystore.model.dto.order.OrderStatusDto;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record GetOrderResponse(
    Integer orderId,
    Integer userId,
    DeliveryDetailsDto deliveryDetailsDto,
    List<OrderDetailsDto> orderDetailsDto,
    OrderStatusDto status,
    Double discount,
    Integer totalAmount,
    LocalDate orderDate,
    String shippingNumber
) {
}
