package com.militarystore.order;

import com.militarystore.entity.order.Order;
import com.militarystore.entity.order.OrderStatus;
import com.militarystore.jooq.tables.records.OrdersRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.militarystore.jooq.Tables.ORDERS;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final DSLContext dslContext;

    public Integer submitOrder(Order order) {
        return dslContext.insertInto(ORDERS)
            .set(ORDERS.USER_ID, order.userId())
            .set(ORDERS.DELIVERY_DETAILS_ID, order.deliveryDetails().id())
            .set(ORDERS.DISCOUNT, order.discount())
            .set(ORDERS.TOTAL_AMOUNT, order.totalAmount())
            .set(ORDERS.ORDER_DATE, order.orderDate())
            .returning(ORDERS.ID)
            .fetchOne(ORDERS.ID);
    }

    public void updateOrderStatusWithShippingNumber(Integer orderId, OrderStatus status, String shippingNumber) {
        dslContext.update(ORDERS)
            .set(ORDERS.ORDER_STATUS, status.name())
            .set(ORDERS.SHIPPING_NUMBER, shippingNumber)
            .where(ORDERS.ID.eq(orderId))
            .execute();
    }

    public void updateOrderStatus(Integer orderId, OrderStatus status) {
        dslContext.update(ORDERS)
            .set(ORDERS.ORDER_STATUS, status.name())
            .where(ORDERS.ID.eq(orderId))
            .execute();
    }

    public List<OrdersRecord> getUserOrders(Integer userId) {
        return dslContext.select(
                ORDERS.ID,
                ORDERS.ORDER_STATUS,
                ORDERS.ORDER_DATE,
                ORDERS.SHIPPING_NUMBER,
                ORDERS.TOTAL_AMOUNT
            )
            .from(ORDERS)
            .where(ORDERS.USER_ID.eq(userId))
            .fetchInto(OrdersRecord.class);
    }

    public OrdersRecord getOrderById(Integer orderId) {
        return dslContext.selectFrom(ORDERS)
            .where(ORDERS.ID.eq(orderId))
            .fetchOne();
    }

    public List<OrdersRecord> getOrdersByStatus(OrderStatus status) {
        return dslContext.selectFrom(ORDERS)
            .where(ORDERS.ORDER_STATUS.eq(status.name()))
            .fetchInto(OrdersRecord.class);
    }

    public boolean isOrderExists(Integer orderId) {
        return dslContext.fetchExists(
            dslContext.selectFrom(ORDERS)
                .where(ORDERS.ID.eq(orderId))
        );
    }
}
