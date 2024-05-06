package com.militarystore.order.mapper;

import com.militarystore.entity.order.OrderDetails;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.jooq.tables.records.OrderDetailsRecord;
import org.jooq.RecordMapper;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.militarystore.jooq.Tables.ORDER_DETAILS;
import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.PRODUCT_STOCK_DETAILS;

@Component
public class OrderDetailsMapper implements RecordMapper<Record, OrderDetails> {

    @Override
    public OrderDetails map(Record orderDetailsRecord) {
        return OrderDetails.builder()
            .orderId(orderDetailsRecord.get(ORDER_DETAILS.ORDER_ID))
            .productStockDetailsId(orderDetailsRecord.get(ORDER_DETAILS.PRODUCT_STOCK_DETAILS_ID))
            .quantity(orderDetailsRecord.get(ORDER_DETAILS.QUANTITY))
            .productName(orderDetailsRecord.get(PRODUCTS.NAME))
            .productPrice(orderDetailsRecord.get(PRODUCTS.PRICE))
            .productSize(ProductSize.valueOf(orderDetailsRecord.get(PRODUCT_STOCK_DETAILS.PRODUCT_SIZE)))
            .build();
    }

    public OrderDetailsRecord toRecord(Integer orderId, OrderDetails orderDetails) {
        OrderDetailsRecord orderDetailsRecord = new OrderDetailsRecord();
        orderDetailsRecord.setOrderId(orderId);
        orderDetailsRecord.setProductStockDetailsId(orderDetails.productStockDetailsId());
        orderDetailsRecord.setQuantity(orderDetails.quantity());

        return orderDetailsRecord;
    }
}
