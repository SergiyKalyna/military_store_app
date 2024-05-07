package com.militarystore.order;

import com.militarystore.entity.order.Order;
import com.militarystore.entity.order.OrderStatus;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.out.order.OrderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOrderServiceTest {

    @Mock
    private OrderPort orderPort;

    private GetOrderService getOrderService;

    @BeforeEach
    void setUp() {
        getOrderService = new GetOrderService(orderPort);
    }

    @Test
    void getUserOrders() {
        var userId = 1;
        var orders = List.of(Order.builder().build());

        when(orderPort.getUserOrders(userId)).thenReturn(orders);

        assertThat(getOrderService.getUserOrders(userId)).isEqualTo(orders);
    }

    @Test
    void getOrdersByStatus() {
        var status = OrderStatus.IN_PROGRESS;
        var orders = List.of(Order.builder().build());

        when(orderPort.getOrdersByStatus(status)).thenReturn(orders);

        assertThat(getOrderService.getOrdersByStatus(status)).isEqualTo(orders);
    }

    @Test
    void getOrderById() {
        var orderId = 1;
        var order = Order.builder().build();

        when(orderPort.isOrderExists(orderId)).thenReturn(true);
        when(orderPort.getOrderById(orderId)).thenReturn(order);

        assertThat(getOrderService.getOrderById(orderId)).isEqualTo(order);
    }

    @Test
    void getOrderById_whenOrderNotFound() {
        var orderId = 1;

        when(orderPort.isOrderExists(orderId)).thenReturn(false);

        assertThrows(MsNotFoundException.class, () -> getOrderService.getOrderById(orderId));
    }
}