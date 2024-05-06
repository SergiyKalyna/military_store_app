package com.militarystore.order.mapper;

import com.militarystore.entity.order.OrderDetails;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.jooq.tables.records.OrderDetailsRecord;
import org.jooq.Record7;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.militarystore.jooq.Tables.ORDER_DETAILS;
import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.PRODUCT_STOCK_DETAILS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderDetailsMapperTest {

    private OrderDetailsMapper orderDetailsMapper;

    @BeforeEach
    void setUp() {
        orderDetailsMapper = new OrderDetailsMapper();
    }

    @Test
    void map() {
        var orderDetailsRecord = mock(Record7.class);
        when(orderDetailsRecord.get(ORDER_DETAILS.ORDER_ID)).thenReturn(1);
        when(orderDetailsRecord.get(PRODUCTS.ID)).thenReturn(111);
        when(orderDetailsRecord.get(ORDER_DETAILS.PRODUCT_STOCK_DETAILS_ID)).thenReturn(2);
        when(orderDetailsRecord.get(ORDER_DETAILS.QUANTITY)).thenReturn(3);
        when(orderDetailsRecord.get(PRODUCTS.NAME)).thenReturn("name");
        when(orderDetailsRecord.get(PRODUCTS.PRICE)).thenReturn(100);
        when(orderDetailsRecord.get(PRODUCT_STOCK_DETAILS.PRODUCT_SIZE)).thenReturn("S");

        var orderDetails = OrderDetails.builder()
            .orderId(1)
            .productId(111)
            .productStockDetailsId(2)
            .quantity(3)
            .productName("name")
            .productPrice(100)
            .productSize(ProductSize.S)
            .build();

        assertThat(orderDetailsMapper.map(orderDetailsRecord)).isEqualTo(orderDetails);
    }

    @Test
    void toRecord() {
        var orderDetails = OrderDetails.builder()
            .productStockDetailsId(2)
            .quantity(3)
            .build();
        var orderId = 1;

        var orderDetailsRecord = new OrderDetailsRecord();
        orderDetailsRecord.setOrderId(1);
        orderDetailsRecord.setProductStockDetailsId(2);
        orderDetailsRecord.setQuantity(3);

        assertThat(orderDetailsMapper.toRecord(orderId, orderDetails)).isEqualTo(orderDetailsRecord);
    }
}