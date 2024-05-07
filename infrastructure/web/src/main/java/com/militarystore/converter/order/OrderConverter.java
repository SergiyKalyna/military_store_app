package com.militarystore.converter.order;

import com.militarystore.converter.delivery.DeliveryDetailsConverter;
import com.militarystore.entity.order.Order;
import com.militarystore.entity.order.OrderDetails;
import com.militarystore.entity.order.OrderStatus;
import com.militarystore.model.dto.order.OrderDetailsDto;
import com.militarystore.model.dto.order.OrderDto;
import com.militarystore.model.dto.order.OrderStatusDto;
import com.militarystore.model.dto.product.ProductSizeDto;
import com.militarystore.model.request.order.SubmitOrderRequest;
import com.militarystore.model.response.order.GetOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderConverter {

    private final DeliveryDetailsConverter deliveryDetailsConverter;

    public Order convertToOrder(SubmitOrderRequest submitOrderRequest, Integer userId) {
        return Order.builder()
            .userId(userId)
            .deliveryDetails(deliveryDetailsConverter.convertToDeliveryDetails(submitOrderRequest.deliveryDetailsDto()))
            .orderDetails(convertToOrderDetails(submitOrderRequest.orderDetailsDto()))
            .discount(submitOrderRequest.discount())
            .totalAmount(submitOrderRequest.totalAmount())
            .orderDate(LocalDate.now())
            .status(OrderStatus.NEW)
            .build();
    }

    public OrderDto convertToOrderDto(Order order) {
        return OrderDto.builder()
            .id(order.id())
            .totalAmount(order.totalAmount())
            .orderDate(order.orderDate())
            .orderStatusDto(OrderStatusDto.valueOf(order.status().name()))
            .shippingNumber(order.shippingNumber())
            .build();
    }

    public GetOrderResponse convertToGetOrderResponse(Order order) {
        var deliveryDetailsDto = deliveryDetailsConverter.convertToDto(order.deliveryDetails());
        var orderDetailsDto = convertToOrderDetailsDto(order.orderDetails());

        return GetOrderResponse.builder()
            .orderId(order.id())
            .userId(order.userId())
            .deliveryDetailsDto(deliveryDetailsDto)
            .orderDetailsDto(orderDetailsDto)
            .status(OrderStatusDto.valueOf(order.status().name()))
            .discount(order.discount())
            .totalAmount(order.totalAmount())
            .orderDate(order.orderDate())
            .shippingNumber(order.shippingNumber())
            .build();
    }

    public OrderStatus convertToOrderStatus(OrderStatusDto orderStatusDto) {
        return OrderStatus.valueOf(orderStatusDto.name());
    }

    private List<OrderDetails> convertToOrderDetails(List<OrderDetailsDto> orderDetailsDto) {
        return orderDetailsDto.stream()
            .map(this::convertToOrderDetails)
            .toList();
    }

    private OrderDetails convertToOrderDetails(OrderDetailsDto orderDetailsDto) {
        return OrderDetails.builder()
            .productId(orderDetailsDto.productId())
            .productStockDetailsId(orderDetailsDto.productStockDetailsId())
            .quantity(orderDetailsDto.productQuantity())
            .productName(orderDetailsDto.productName())
            .build();
    }

    private List<OrderDetailsDto> convertToOrderDetailsDto(List<OrderDetails> orderDetails) {
        return orderDetails.stream()
            .map(this::convertToOrderDetailsDto)
            .toList();
    }

    private OrderDetailsDto convertToOrderDetailsDto(OrderDetails orderDetails) {
        return OrderDetailsDto.builder()
            .productId(orderDetails.productId())
            .productStockDetailsId(orderDetails.productStockDetailsId())
            .productQuantity(orderDetails.quantity())
            .productName(orderDetails.productName())
            .productPrice(orderDetails.productPrice())
            .productSizeDto(ProductSizeDto.valueOf(orderDetails.productSize().name()))
            .totalAmount(orderDetails.quantity() * orderDetails.productPrice())
            .build();
    }
}
