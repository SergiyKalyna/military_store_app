package com.militarystore.order;

import com.militarystore.entity.order.Order;
import com.militarystore.entity.order.OrderStatus;
import com.militarystore.order.mapper.OrderMapper;
import com.militarystore.port.out.delivery.DeliveryDetailsPort;
import com.militarystore.port.out.order.OrderDetailsPort;
import com.militarystore.port.out.order.OrderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.militarystore.jooq.Tables.ORDERS;

@Component
@RequiredArgsConstructor
public class OrderAdapter implements OrderPort {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderDetailsPort orderDetailsPort;
    private final DeliveryDetailsPort deliveryDetailsPort;

    @Override
    public Integer submitOrder(Integer deliveryDetailsId, Order order) {
        return orderRepository.submitOrder(deliveryDetailsId, order);
    }

    @Override
    public void updateOrderStatusWithShippingNumber(Integer orderId, OrderStatus status, String shippingNumber) {
        orderRepository.updateOrderStatusWithShippingNumber(orderId, status, shippingNumber);
    }

    @Override
    public void updateOrderStatus(Integer orderId, OrderStatus status) {
        orderRepository.updateOrderStatus(orderId, status);
    }

    @Override
    public List<Order> getUserOrders(Integer userId) {
        return orderRepository.getUserOrders(userId).stream()
            .map(orderMapper::map)
            .toList();
    }

    @Override
    public Order getOrderById(Integer orderId) {
        var orderRecord = orderRepository.getOrderById(orderId);
        var deliveryDetails = deliveryDetailsPort.getDeliveryDetails(orderRecord.get(ORDERS.DELIVERY_DETAILS_ID));
        var orderDetails = orderDetailsPort.getOrderDetailsByOrderId(orderId);

        return orderMapper.mapToOrderBuilder(orderRecord)
            .deliveryDetails(deliveryDetails)
            .orderDetails(orderDetails)
            .build();
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.getOrdersByStatus(status).stream()
            .map(orderMapper::map)
            .toList();
    }

    @Override
    public boolean isOrderExists(Integer id) {
        return orderRepository.isOrderExists(id);
    }
}
