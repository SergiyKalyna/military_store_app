package com.militarystore.port.out.order;

import com.militarystore.entity.order.Order;
import com.militarystore.entity.order.OrderStatus;

import java.util.List;

public interface OrderPort {

    Integer submitOrder(Order order);

    void updateOrderStatusWithShippingNumber(Integer orderId, OrderStatus status, String shippingNumber);

    void updateOrderStatus(Integer orderId, OrderStatus status);

    List<Order> getUserOrders(Integer userId);

    Order getOrderById(Integer orderId);

    List<Order> getOrdersByStatus(OrderStatus status);

    boolean isOrderExists(Integer id);
}
