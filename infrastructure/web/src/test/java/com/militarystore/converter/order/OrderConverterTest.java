package com.militarystore.converter.order;

import com.militarystore.converter.delivery.DeliveryDetailsConverter;
import com.militarystore.entity.delivery.DeliveryDetails;
import com.militarystore.entity.order.Order;
import com.militarystore.entity.order.OrderDetails;
import com.militarystore.entity.order.OrderStatus;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.model.dto.delivery.DeliveryDetailsDto;
import com.militarystore.model.dto.order.OrderDetailsDto;
import com.militarystore.model.dto.order.OrderDto;
import com.militarystore.model.dto.order.OrderStatusDto;
import com.militarystore.model.dto.product.ProductSizeDto;
import com.militarystore.model.request.order.SubmitOrderRequest;
import com.militarystore.model.response.order.GetOrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderConverterTest {

    private static final Integer ORDER_ID = 1;
    private static final Integer USER_ID = 2;
    private static final Integer PRODUCT_ID = 3;
    private static final Integer PRODUCT_STOCK_DETAILS_ID = 4;

    @Mock
    private DeliveryDetailsConverter deliveryDetailsConverter;

    private OrderConverter orderConverter;

    @BeforeEach
    void setUp() {
        orderConverter = new OrderConverter(deliveryDetailsConverter);
    }

    @Test
    void convertToOrder() {
        var deliveryDetails = DeliveryDetails.builder().build();
        var deliveryDetailsDto = DeliveryDetailsDto.builder().build();
        var submitOrderRequest = SubmitOrderRequest.builder()
            .deliveryDetailsDto(deliveryDetailsDto)
            .orderDetailsDto(List.of(
                OrderDetailsDto.builder()
                    .productId(PRODUCT_ID)
                    .productStockDetailsId(PRODUCT_STOCK_DETAILS_ID)
                    .productName("product")
                    .productQuantity(11)
                    .build()
            ))
            .discount(0.02)
            .discountCode("code")
            .totalAmount(100)
            .build();

        when(deliveryDetailsConverter.convertToDeliveryDetails(deliveryDetailsDto)).thenReturn(deliveryDetails);

        var expectedOrder = Order.builder()
            .userId(USER_ID)
            .deliveryDetails(deliveryDetailsConverter.convertToDeliveryDetails(deliveryDetailsDto))
            .orderDetails(List.of(
                OrderDetails.builder()
                    .productId(PRODUCT_ID)
                    .productStockDetailsId(PRODUCT_STOCK_DETAILS_ID)
                    .productName("product")
                    .quantity(11)
                    .build()
            ))
            .discount(0.02)
            .totalAmount(100)
            .orderDate(LocalDate.now())
            .status(OrderStatus.NEW)
            .build();

        assertThat(orderConverter.convertToOrder(submitOrderRequest, USER_ID)).isEqualTo(expectedOrder);
    }

    @Test
    void convertToOrderDto() {
        var order = Order.builder()
            .id(ORDER_ID)
            .totalAmount(100)
            .orderDate(LocalDate.EPOCH)
            .status(OrderStatus.NEW)
            .shippingNumber("number")
            .build();

        var expectedOrderDto = OrderDto.builder()
            .id(ORDER_ID)
            .totalAmount(100)
            .orderDate(LocalDate.EPOCH)
            .orderStatusDto(OrderStatusDto.NEW)
            .shippingNumber("number")
            .build();

        assertThat(orderConverter.convertToOrderDto(order)).isEqualTo(expectedOrderDto);
    }

    @Test
    void convertToGetOrderResponse() {
        var deliveryDetails = DeliveryDetails.builder().build();
        var deliveryDetailsDto = DeliveryDetailsDto.builder().build();

        var order = Order.builder()
            .id(ORDER_ID)
            .userId(USER_ID)
            .deliveryDetails(deliveryDetails)
            .orderDetails(List.of(
                OrderDetails.builder()
                    .productId(PRODUCT_ID)
                    .productStockDetailsId(PRODUCT_STOCK_DETAILS_ID)
                    .productName("product")
                    .quantity(11)
                    .productPrice(100)
                    .productSize(ProductSize.S)
                    .build()
            ))
            .discount(0.02)
            .totalAmount(100)
            .orderDate(LocalDate.EPOCH)
            .status(OrderStatus.NEW)
            .shippingNumber("number")
            .build();

        when(deliveryDetailsConverter.convertToDto(deliveryDetails)).thenReturn(deliveryDetailsDto);

        var expectedResponse = GetOrderResponse.builder()
            .orderId(ORDER_ID)
            .userId(USER_ID)
            .deliveryDetailsDto(deliveryDetailsDto)
            .orderDetailsDto(List.of(
                OrderDetailsDto.builder()
                    .productId(PRODUCT_ID)
                    .productStockDetailsId(PRODUCT_STOCK_DETAILS_ID)
                    .productName("product")
                    .productQuantity(11)
                    .productPrice(100)
                    .productSizeDto(ProductSizeDto.S)
                    .totalAmount(1100)
                    .build()
            ))
            .status(OrderStatusDto.NEW)
            .discount(0.02)
            .totalAmount(100)
            .orderDate(LocalDate.EPOCH)
            .shippingNumber("number")
            .build();

        assertThat(orderConverter.convertToGetOrderResponse(order)).isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @EnumSource(OrderStatusDto.class)
    void convertToOrderStatus(OrderStatusDto orderStatusDto) {
        assertDoesNotThrow(() -> OrderStatus.valueOf(orderStatusDto.name()));
    }

    @ParameterizedTest
    @EnumSource(OrderStatus.class)
    void convertToOrderStatusDto(OrderStatus orderStatus) {
        assertDoesNotThrow(() -> OrderStatusDto.valueOf(orderStatus.name()));
    }
}