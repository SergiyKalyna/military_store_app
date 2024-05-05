package com.militarystore.order.mapper;

import com.militarystore.entity.order.Order;
import com.militarystore.entity.order.OrderStatus;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.militarystore.jooq.Tables.ORDERS;

@Component
public class OrderMapper implements RecordMapper<Record, Order> {

    @Override
    public Order map(Record orderRecord) {
        return Order.builder()
            .id(orderRecord.get(ORDERS.ID))
            .orderDate(orderRecord.get(ORDERS.ORDER_DATE))
            .totalAmount(orderRecord.get(ORDERS.TOTAL_AMOUNT))
            .status(OrderStatus.valueOf(orderRecord.get(ORDERS.ORDER_STATUS)))
            .shippingNumber(orderRecord.get(ORDERS.SHIPPING_NUMBER))
            .build();
    }

    public Order.OrderBuilder mapToOrderBuilder(Record orderRecord) {
        return Order.builder()
            .id(orderRecord.get(ORDERS.ID))
            .userId(orderRecord.get(ORDERS.USER_ID))
            .discount(orderRecord.get(ORDERS.DISCOUNT))
            .orderDate(orderRecord.get(ORDERS.ORDER_DATE))
            .totalAmount(orderRecord.get(ORDERS.TOTAL_AMOUNT))
            .status(OrderStatus.valueOf(orderRecord.get(ORDERS.ORDER_STATUS)))
            .shippingNumber(orderRecord.get(ORDERS.SHIPPING_NUMBER));
    }
}
