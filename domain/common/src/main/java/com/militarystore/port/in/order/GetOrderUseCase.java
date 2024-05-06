package com.militarystore.port.in.order;

import com.militarystore.entity.order.Order;
import com.militarystore.entity.order.OrderStatus;

import java.util.List;

public interface GetOrderUseCase {

    List<Order> getUserOrders(Integer userId);

    Order getOrderById(Integer orderId);

    List<Order> getOrdersByStatus(OrderStatus status);
}
