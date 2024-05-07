package com.militarystore.port.in.order;

import com.militarystore.entity.order.Order;

public interface SubmitOrderUseCase {

    Integer submitOrder(Order order, String discountCode);
}