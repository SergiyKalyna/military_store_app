package com.militarystore.port.in.order;

import com.militarystore.entity.order.OrderStatus;

public interface UpdateOrderUseCase {

    void updateOrderStatus(Integer orderId, OrderStatus status);

    void updateOrderStatusWithShippingNumber(Integer orderId, OrderStatus status, String shippingNumber);
}
