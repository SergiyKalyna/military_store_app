package com.militarystore.order;

import com.militarystore.converter.order.OrderConverter;
import com.militarystore.model.dto.order.OrderDto;
import com.militarystore.model.dto.order.OrderStatusDto;
import com.militarystore.model.request.order.SubmitOrderRequest;
import com.militarystore.model.response.order.GetOrderResponse;
import com.militarystore.port.in.order.GetOrderUseCase;
import com.militarystore.port.in.order.SubmitOrderUseCase;
import com.militarystore.port.in.order.UpdateOrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final SubmitOrderUseCase submitOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;
    private final OrderConverter orderConverter;

    @PostMapping("/user/{userId}")
    public Integer submitOrder(
        @PathVariable("userId") Integer userId,
        @RequestBody SubmitOrderRequest submitOrderRequest
    ) {
        var order = orderConverter.convertToOrder(submitOrderRequest, userId);

        return submitOrderUseCase.submitOrder(order, submitOrderRequest.discountCode());
    }

    @PutMapping("/{orderId}")
    public void updateOrderStatus(
        @PathVariable("orderId") Integer orderId,
        @RequestParam("status") OrderStatusDto status
    ) {
        var orderStatus = orderConverter.convertToOrderStatus(status);

        updateOrderUseCase.updateOrderStatus(orderId, orderStatus);
    }

    @PutMapping("/{orderId}/shipping")
    public void updateOrderStatusWithShippingNumber(
        @PathVariable("orderId") Integer orderId,
        @RequestParam("status") OrderStatusDto status,
        @RequestParam("shippingNumber") String shippingNumber
    ) {
        var orderStatus = orderConverter.convertToOrderStatus(status);

        updateOrderUseCase.updateOrderStatusWithShippingNumber(orderId, orderStatus, shippingNumber);
    }

    @GetMapping("/user/{userId}")
    public List<OrderDto> getUserOrders(@PathVariable("userId") Integer userId) {
        return getOrderUseCase.getUserOrders(userId).stream()
            .map(orderConverter::convertToOrderDto)
            .toList();
    }

    @GetMapping("/status")
    public List<OrderDto> getOrdersByStatus(@RequestParam("status") OrderStatusDto status) {
        var orderStatus = orderConverter.convertToOrderStatus(status);

        return getOrderUseCase.getOrdersByStatus(orderStatus).stream()
            .map(orderConverter::convertToOrderDto)
            .toList();
    }

    @GetMapping("/{orderId}")
    public GetOrderResponse getOrderById(@PathVariable("orderId") Integer orderId) {
        var order = getOrderUseCase.getOrderById(orderId);

        return orderConverter.convertToGetOrderResponse(order);
    }
}