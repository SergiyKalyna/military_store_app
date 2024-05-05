package com.militarystore.order;

import com.militarystore.entity.delivery.DeliveryDetails;
import com.militarystore.entity.order.Order;
import com.militarystore.entity.order.OrderDetails;
import com.militarystore.entity.order.OrderStatus;
import com.militarystore.jooq.tables.records.OrdersRecord;
import com.militarystore.order.mapper.OrderMapper;
import com.militarystore.port.out.delivery.DeliveryDetailsPort;
import com.militarystore.port.out.order.OrderDetailsPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.militarystore.jooq.Tables.ORDERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderAdapterTest {

    private static final int ORDER_ID = 1;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderDetailsPort orderDetailsPort;

    @Mock
    private DeliveryDetailsPort deliveryDetailsPort;

    private OrderAdapter orderAdapter;

    @BeforeEach
    void setUp() {
        orderAdapter = new OrderAdapter(orderRepository, orderMapper, orderDetailsPort, deliveryDetailsPort);
    }

    @Test
    void addOrder() {
        var order = Order.builder().build();

        orderAdapter.addOrder(order);

        verify(orderRepository).submitOrder(order);
    }

    @Test
    void updateOrderStatusWithShippingNumber() {
        orderAdapter.updateOrderStatusWithShippingNumber(1, OrderStatus.NEW, "123");

        verify(orderRepository).updateOrderStatusWithShippingNumber(1, OrderStatus.NEW, "123");
    }

    @Test
    void updateOrderStatus() {
        orderAdapter.updateOrderStatus(1, OrderStatus.NEW);

        verify(orderRepository).updateOrderStatus(1, OrderStatus.NEW);
    }

    @Test
    void getUserOrders() {
        var order = Order.builder().build();
        var orderRecord = new OrdersRecord();

        when(orderRepository.getUserOrders(ORDER_ID)).thenReturn(List.of(orderRecord));
        when(orderMapper.map(orderRecord)).thenReturn(order);

        assertThat(orderAdapter.getUserOrders(ORDER_ID)).isEqualTo(List.of(order));
    }

    @Test
    void getOrderById() {
        var order = Order.builder();
        var orderRecord = new OrdersRecord();
        var orderDetails = List.of(OrderDetails.builder().build());
        var deliveryDetails = DeliveryDetails.builder().build();
        var assembledOrder = order
            .deliveryDetails(deliveryDetails)
            .orderDetails(orderDetails)
            .build();

        when(orderRepository.getOrderById(ORDER_ID)).thenReturn(orderRecord);
        when(deliveryDetailsPort.getDeliveryDetails(orderRecord.get(ORDERS.DELIVERY_DETAILS_ID))).thenReturn(deliveryDetails);
        when(orderDetailsPort.getOrderDetailsByOrderId(ORDER_ID)).thenReturn(orderDetails);
        when(orderMapper.mapToOrderBuilder(orderRecord)).thenReturn(order);

        assertThat(orderAdapter.getOrderById(ORDER_ID)).isEqualTo(assembledOrder);
    }

    @Test
    void getOrdersByStatus() {
        var order = Order.builder().build();
        var orderRecord = new OrdersRecord();

        when(orderRepository.getOrdersByStatus(OrderStatus.NEW)).thenReturn(List.of(orderRecord));
        when(orderMapper.map(orderRecord)).thenReturn(order);

        assertThat(orderAdapter.getOrdersByStatus(OrderStatus.NEW)).isEqualTo(List.of(order));
    }

    @Test
    void isOrderExists() {
        when(orderRepository.isOrderExists(ORDER_ID)).thenReturn(true);

        assertTrue(orderAdapter.isOrderExists(ORDER_ID));
    }
}