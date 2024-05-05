package com.militarystore.order.mapper;

import com.militarystore.entity.order.Order;
import com.militarystore.entity.order.OrderStatus;
import com.militarystore.jooq.tables.records.OrdersRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.militarystore.jooq.Tables.ORDERS;
import static org.assertj.core.api.Assertions.assertThat;

class OrderMapperTest {

    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        orderMapper = new OrderMapper();
    }

    @Test
    void map() {
        var orderRecord = new OrdersRecord();
        orderRecord.set(ORDERS.ID, 1);
        orderRecord.set(ORDERS.ORDER_DATE, LocalDate.EPOCH);
        orderRecord.set(ORDERS.TOTAL_AMOUNT, 100);
        orderRecord.set(ORDERS.ORDER_STATUS, "NEW");
        orderRecord.set(ORDERS.SHIPPING_NUMBER, "123");

        var order = Order.builder()
            .id(1)
            .orderDate(LocalDate.EPOCH)
            .totalAmount(100)
            .status(OrderStatus.NEW)
            .shippingNumber("123")
            .build();

        assertThat(orderMapper.map(orderRecord)).isEqualTo(order);
    }

    @Test
    void mapToOrderBuilder() {
        var orderRecord = new OrdersRecord();
        orderRecord.set(ORDERS.ID, 1);
        orderRecord.set(ORDERS.USER_ID, 2);
        orderRecord.set(ORDERS.DISCOUNT, 0.03);
        orderRecord.set(ORDERS.ORDER_DATE, LocalDate.EPOCH);
        orderRecord.set(ORDERS.TOTAL_AMOUNT, 100);
        orderRecord.set(ORDERS.ORDER_STATUS, "NEW");
        orderRecord.set(ORDERS.SHIPPING_NUMBER, "123");

        var expectedOrder = Order.builder()
            .id(1)
            .userId(2)
            .discount(0.03)
            .orderDate(LocalDate.EPOCH)
            .totalAmount(100)
            .status(OrderStatus.NEW)
            .shippingNumber("123")
            .build();

        var actualResult = orderMapper.mapToOrderBuilder(orderRecord).build();

        assertThat(actualResult).isEqualTo(expectedOrder);
    }
}