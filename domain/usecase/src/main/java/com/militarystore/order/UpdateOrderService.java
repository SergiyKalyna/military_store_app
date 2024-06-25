package com.militarystore.order;

import com.militarystore.email.EmailSendingService;
import com.militarystore.entity.order.OrderStatus;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.in.order.UpdateOrderUseCase;
import com.militarystore.port.out.order.OrderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateOrderService implements UpdateOrderUseCase {

    private final OrderPort orderPort;
    private final EmailSendingService emailSendingService;

    @Override
    public void updateOrderStatus(Integer orderId, OrderStatus status) {
        checkIfOrderExists(orderId);

        orderPort.updateOrderStatus(orderId, status);
        log.info("Order with id '{}' has changed status to - {}", orderId, status.name());

        emailSendingService.sendEmail(
            orderId,
            format("your order with id #%d has changed status to '%s'", orderId, status.getStatus())
        );
    }

    @Override
    public void updateOrderStatusWithShippingNumber(Integer orderId, OrderStatus status, String shippingNumber) {
        validateShippingNumber(shippingNumber);
        checkIfOrderExists(orderId);

        orderPort.updateOrderStatusWithShippingNumber(orderId, status, shippingNumber);
        log.info("Order with id '{}' has changed status to - {} and shipping number to - {}", orderId, status.name(), shippingNumber);

        emailSendingService.sendEmail(
            orderId,
            format("your order with id #%d has changed status to '%s', your shipping number - %s", orderId, status.getStatus(), shippingNumber)
        );
    }

    private void checkIfOrderExists(Integer orderId) {
        if (!orderPort.isOrderExists(orderId)) {
            throw new MsNotFoundException("Order with id " + orderId + " not found");
        }
    }

    private void validateShippingNumber(String shippingNumber) {
        if (isNull(shippingNumber) || shippingNumber.isBlank()) {
            throw new MsValidationException("Shipping number should not be empty");
        }
    }
}
