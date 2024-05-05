package com.militarystore.port.out.order;

import com.militarystore.entity.order.OrderDetails;

import java.util.List;

public interface OrderDetailsPort {

    void addOrderDetails(List<OrderDetails> orderDetails);

    List<OrderDetails> getOrderDetailsByOrderId(Integer orderId);
}
