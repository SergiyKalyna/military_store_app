package com.militarystore.order;

import com.militarystore.delivery.DeliveryDetailsValidator;
import com.militarystore.entity.delivery.DeliveryDetails;
import com.militarystore.entity.order.Order;
import com.militarystore.entity.order.OrderDetails;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.in.order.SubmitOrderUseCase;
import com.militarystore.port.in.product.ProductOrderUseCase;
import com.militarystore.port.out.delivery.DeliveryDetailsPort;
import com.militarystore.port.out.discount.DiscountPort;
import com.militarystore.port.out.order.OrderDetailsPort;
import com.militarystore.port.out.order.OrderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmitOrderService implements SubmitOrderUseCase {

    private final OrderPort orderPort;
    private final OrderDetailsPort orderDetailsPort;
    private final DeliveryDetailsPort deliveryDetailsPort;
    private final DeliveryDetailsValidator deliveryDetailsValidator;
    private final ProductOrderUseCase productOrderUseCase;
    private final DiscountPort discountPort;

    @Override
    public Integer submitOrder(Order order, String discountCode) {
        validateOrder(order.orderDetails(), order.deliveryDetails());

        var deliveryDetailsId = deliveryDetailsPort.addDeliveryDetails(order.deliveryDetails());
        var orderId = orderPort.submitOrder(deliveryDetailsId, order);
        orderDetailsPort.addOrderDetails(orderId, order.orderDetails());

        chargeDiscountLimits(order.userId(), discountCode);
        updateProductStock(order.orderDetails());

        log.info("Order with id {} was submitted by user with id {}", orderId, order.userId());

        return orderId;
    }

    private void validateOrder(List<OrderDetails> orderDetails, DeliveryDetails deliveryDetails) {
        validateOrderDetails(orderDetails);
        deliveryDetailsValidator.validateDeliveryDetails(deliveryDetails);
    }

    private void validateOrderDetails(List<OrderDetails> orderDetails) {
        orderDetails.forEach(orderDetail -> {
            if (!productOrderUseCase.isEnoughProductStockAvailability(orderDetail.productStockDetailsId(), orderDetail.quantity())) {
                throw new MsValidationException(
                    format("Not enough product stock availability(%d) for the %s product with stock details id - %d",
                        orderDetail.quantity(), orderDetail.productName(), orderDetail.productStockDetailsId()
                    )
                );
            }
        });
    }

    private void chargeDiscountLimits(Integer userId, String discountCode) {
        if (Objects.nonNull(discountCode)) {
            discountPort.updateDiscountUsageLimit(discountCode, userId);
        }
    }

    private void updateProductStock(List<OrderDetails> orderDetails) {
        orderDetails.forEach(orderDetail -> productOrderUseCase.updateProductStockAvailability(
                orderDetail.productId(), orderDetail.productStockDetailsId(), orderDetail.quantity()
            )
        );
    }
}
