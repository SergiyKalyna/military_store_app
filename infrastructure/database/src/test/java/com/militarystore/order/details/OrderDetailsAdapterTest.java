package com.militarystore.order.details;

import com.militarystore.entity.order.OrderDetails;
import com.militarystore.jooq.tables.records.OrderDetailsRecord;
import com.militarystore.order.mapper.OrderDetailsMapper;
import org.jooq.Record7;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderDetailsAdapterTest {

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @Mock
    private OrderDetailsMapper orderDetailsMapper;

    private OrderDetailsAdapter orderDetailsAdapter;

    @BeforeEach
    void setUp() {
        orderDetailsAdapter = new OrderDetailsAdapter(orderDetailsRepository, orderDetailsMapper);
    }

    @Test
    void addOrderDetails() {
        var orderDetails = OrderDetails.builder().build();
        var orderDetailsRecord = new OrderDetailsRecord();
        var orderId = 1;

        when(orderDetailsMapper.toRecord(orderId, orderDetails)).thenReturn(orderDetailsRecord);

        orderDetailsAdapter.addOrderDetails(orderId, List.of(orderDetails));

        verify(orderDetailsRepository).addOrderDetails(List.of(orderDetailsRecord));
    }

    @Test
    void getOrderDetailsByOrderId() {
        var orderId = 1;
        var orderDetails = OrderDetails.builder().build();
        var orderDetailsRecord = mock(Record7.class);

        when(orderDetailsRepository.getOrderDetailsByOrderId(orderId)).thenReturn(List.of(orderDetailsRecord));
        when(orderDetailsMapper.map(orderDetailsRecord)).thenReturn(orderDetails);

        assertThat(orderDetailsAdapter.getOrderDetailsByOrderId(orderId)).isEqualTo(List.of(orderDetails));
    }
}