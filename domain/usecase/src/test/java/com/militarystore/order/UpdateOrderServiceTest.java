package com.militarystore.order;

import com.militarystore.entity.order.OrderStatus;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.out.order.OrderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateOrderServiceTest {

    private static final Integer ORDER_ID = 1;

    @Mock
    private OrderPort orderPort;

    private UpdateOrderService updateOrderService;

    @BeforeEach
    void setUp() {
        updateOrderService = new UpdateOrderService(orderPort);
    }

    @Test
    void updateOrderStatus() {
        when(orderPort.isOrderExists(ORDER_ID)).thenReturn(true);

        updateOrderService.updateOrderStatus(ORDER_ID, OrderStatus.IN_PROGRESS);

        verify(orderPort).updateOrderStatus(ORDER_ID, OrderStatus.IN_PROGRESS);
    }

    @Test
    void updateOrderStatus_whenOrderNotExists() {
        when(orderPort.isOrderExists(ORDER_ID)).thenReturn(false);

        assertThrows(MsNotFoundException.class, () -> updateOrderService.updateOrderStatus(ORDER_ID, OrderStatus.IN_PROGRESS));
    }

    @Test
    void updateOrderStatusWithShippingNumber() {
        var shippingNumber = "123456";

        when(orderPort.isOrderExists(ORDER_ID)).thenReturn(true);

        updateOrderService.updateOrderStatusWithShippingNumber(ORDER_ID, OrderStatus.SHIPPED, shippingNumber);

        verify(orderPort).updateOrderStatusWithShippingNumber(ORDER_ID, OrderStatus.SHIPPED, shippingNumber);
    }

    @Test
    void updateOrderStatusWithShippingNumber_whenOrderNotExists() {
        var shippingNumber = "123456";

        when(orderPort.isOrderExists(ORDER_ID)).thenReturn(false);

        assertThrows(MsNotFoundException.class, () ->
            updateOrderService.updateOrderStatusWithShippingNumber(ORDER_ID, OrderStatus.SHIPPED, shippingNumber)
        );
    }

    @Test
    void updateOrderStatusWithShippingNumber_whenShippingNumberIsEmpty() {
        var shippingNumber = "";

        assertThrows(MsValidationException.class, () ->
            updateOrderService.updateOrderStatusWithShippingNumber(ORDER_ID, OrderStatus.SHIPPED, shippingNumber)
        );
    }

    @Test
    void updateOrderStatusWithShippingNumber_whenShippingNumberIsNull() {
        assertThrows(MsValidationException.class, () ->
            updateOrderService.updateOrderStatusWithShippingNumber(ORDER_ID, OrderStatus.SHIPPED, null)
        );
    }
}