package com.militarystore.order.details;

import com.militarystore.entity.order.OrderDetails;
import com.militarystore.order.mapper.OrderDetailsMapper;
import com.militarystore.port.out.order.OrderDetailsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderDetailsAdapter implements OrderDetailsPort {

    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderDetailsMapper orderDetailsMapper;

    @Override
    public void addOrderDetails(Integer orderId, List<OrderDetails> orderDetails) {
        var orderDetailsRecord = orderDetails.stream()
            .map(details -> orderDetailsMapper.toRecord(orderId, details))
            .toList();

        orderDetailsRepository.addOrderDetails(orderDetailsRecord);
    }

    @Override
    public List<OrderDetails> getOrderDetailsByOrderId(Integer orderId) {
        return orderDetailsRepository.getOrderDetailsByOrderId(orderId).stream()
            .map(orderDetailsMapper::map)
            .toList();
    }
}
