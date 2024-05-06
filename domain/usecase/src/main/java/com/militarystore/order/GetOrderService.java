package com.militarystore.order;

import com.militarystore.entity.order.Order;
import com.militarystore.entity.order.OrderStatus;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.in.order.GetOrderUseCase;
import com.militarystore.port.out.order.OrderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetOrderService implements GetOrderUseCase {

    private final OrderPort orderPort;

    @Override
    public List<Order> getUserOrders(Integer userId) {
        log.info("Getting orders for user with id '{}'", userId);
        return orderPort.getUserOrders(userId);
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {
        log.info("Getting orders by status - '{}'", status.name());
        return orderPort.getOrdersByStatus(status);
    }

    @Override
    public Order getOrderById(Integer orderId) {
        checkIfOrderExists(orderId);

        log.info("Getting order with id '{}'", orderId);

        return orderPort.getOrderById(orderId);
    }

    private void checkIfOrderExists(Integer orderId) {
        if (!orderPort.isOrderExists(orderId)) {
            throw new MsNotFoundException("Order with id " + orderId + " not found");
        }
    }
}
