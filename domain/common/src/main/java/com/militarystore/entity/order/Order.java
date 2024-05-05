package com.militarystore.entity.order;

import com.militarystore.entity.delivery.DeliveryDetails;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record Order(
    Integer id,
    Integer userId,
    DeliveryDetails deliveryDetails,
    List<OrderDetails> orderDetails,
    Double discount,
    Integer totalAmount,
    LocalDate orderDate,
    OrderStatus status,
    String shippingNumber
) {
}
