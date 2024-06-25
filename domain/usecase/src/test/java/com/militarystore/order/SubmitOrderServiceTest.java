package com.militarystore.order;

import com.militarystore.delivery.DeliveryDetailsValidator;
import com.militarystore.email.EmailSendingService;
import com.militarystore.entity.delivery.DeliveryDetails;
import com.militarystore.entity.order.Order;
import com.militarystore.entity.order.OrderDetails;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.in.product.ProductOrderUseCase;
import com.militarystore.port.out.delivery.DeliveryDetailsPort;
import com.militarystore.port.out.discount.DiscountPort;
import com.militarystore.port.out.order.OrderDetailsPort;
import com.militarystore.port.out.order.OrderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmitOrderServiceTest {

    private static final String DISCOUNT_CODE = "DISCOUNT_CODE";

    @Mock
    private OrderPort orderPort;

    @Mock
    private OrderDetailsPort orderDetailsPort;

    @Mock
    private DeliveryDetailsPort deliveryDetailsPort;

    @Mock
    private DeliveryDetailsValidator deliveryDetailsValidator;

    @Mock
    private ProductOrderUseCase productOrderUseCase;

    @Mock
    private DiscountPort discountPort;

    @Mock
    private EmailSendingService emailSendingService;

    private SubmitOrderService submitOrderService;

    @BeforeEach
    void setUp() {
        submitOrderService = new SubmitOrderService(
            orderPort,
            orderDetailsPort,
            deliveryDetailsPort,
            deliveryDetailsValidator,
            productOrderUseCase,
            discountPort,
            emailSendingService
        );
    }

    @Test
    void submitOrder_whenProductStockIsNotAvailable_shouldThrowException() {
        var order = Order.builder()
            .orderDetails(
                List.of(
                    OrderDetails.builder()
                        .productStockDetailsId(1)
                        .quantity(1)
                        .productName("Product")
                        .build())
            )
            .build();

        when(productOrderUseCase.isEnoughProductStockAvailability(1, 1))
            .thenReturn(false);

        assertThrows(
            MsValidationException.class,
            () -> submitOrderService.submitOrder(order, DISCOUNT_CODE)
        );

        verifyNoInteractions(emailSendingService);
    }

    @Test
    void submitOrder_whenDeliveryDetailsAreInvalid_shouldThrowException() {
        var order = Order.builder()
            .orderDetails(
                List.of(
                    OrderDetails.builder()
                        .productStockDetailsId(1)
                        .quantity(1)
                        .productName("Product")
                        .build())
            )
            .deliveryDetails(DeliveryDetails.builder().build())
            .build();

        when(productOrderUseCase.isEnoughProductStockAvailability(1, 1))
            .thenReturn(true);

        doThrow(MsValidationException.class)
            .when(deliveryDetailsValidator)
            .validateDeliveryDetails(order.deliveryDetails());

        assertThrows(
            MsValidationException.class,
            () -> submitOrderService.submitOrder(order, DISCOUNT_CODE)
        );

        verifyNoInteractions(emailSendingService);
    }

    @Test
    void submitOrder_whenDiscountCodeIsNotNull_shouldUpdateDiscountUsageLimit() {
        var order = Order.builder()
            .orderDetails(
                List.of(
                    OrderDetails.builder()
                        .productId(10)
                        .productStockDetailsId(1)
                        .quantity(1)
                        .productName("Product")
                        .build())
            )
            .deliveryDetails(DeliveryDetails.builder().build())
            .userId(1)
            .build();

        when(productOrderUseCase.isEnoughProductStockAvailability(1, 1))
            .thenReturn(true);
        when(deliveryDetailsPort.addDeliveryDetails(order.deliveryDetails()))
            .thenReturn(1);
        when(orderPort.submitOrder(1, order))
            .thenReturn(11);

        submitOrderService.submitOrder(order, DISCOUNT_CODE);

        verify(orderDetailsPort).addOrderDetails(11, order.orderDetails());
        verify(discountPort).updateDiscountUsageLimit(DISCOUNT_CODE, order.userId());
        verify(productOrderUseCase).updateProductStockAvailability(10, 1, 1);
        verify(emailSendingService).sendEmail(11, "your order has been submitted successfully, your order id is - 11");
    }

    @Test
    void submitOrder_whenDiscountCodeIsNull_shouldNotUpdateDiscountUsageLimit() {
        var order = Order.builder()
            .orderDetails(
                List.of(
                    OrderDetails.builder()
                        .productId(10)
                        .productStockDetailsId(1)
                        .quantity(1)
                        .productName("Product")
                        .build())
            )
            .deliveryDetails(DeliveryDetails.builder().build())
            .userId(1)
            .build();

        when(productOrderUseCase.isEnoughProductStockAvailability(1, 1))
            .thenReturn(true);
        when(deliveryDetailsPort.addDeliveryDetails(order.deliveryDetails()))
            .thenReturn(1);
        when(orderPort.submitOrder(1, order))
            .thenReturn(11);

        submitOrderService.submitOrder(order, null);

        verify(orderDetailsPort).addOrderDetails(11, order.orderDetails());
        verify(productOrderUseCase).updateProductStockAvailability(10, 1, 1);
        verify(emailSendingService).sendEmail(11, "your order has been submitted successfully, your order id is - 11");
        verifyNoInteractions(discountPort);
    }
}