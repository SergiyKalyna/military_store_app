package com.militarystore.order.details;

import com.militarystore.jooq.tables.records.OrderDetailsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record7;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.militarystore.jooq.Tables.ORDER_DETAILS;
import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.PRODUCT_STOCK_DETAILS;

@Repository
@RequiredArgsConstructor
public class OrderDetailsRepository {

    private final DSLContext dslContext;

    public void addOrderDetails(List<OrderDetailsRecord> orderDetailsRecords) {
        dslContext.batchInsert(orderDetailsRecords).execute();
    }

    public List<Record7<Integer, Integer, Integer, Integer, String, String, Integer>> getOrderDetailsByOrderId(Integer orderId) {
        return dslContext.select(
                ORDER_DETAILS.ORDER_ID,
                ORDER_DETAILS.PRODUCT_STOCK_DETAILS_ID,
                ORDER_DETAILS.QUANTITY,
                PRODUCTS.ID,
                PRODUCT_STOCK_DETAILS.PRODUCT_SIZE,
                PRODUCTS.NAME,
                PRODUCTS.PRICE
            )
            .from(ORDER_DETAILS)
            .innerJoin(PRODUCT_STOCK_DETAILS).on(ORDER_DETAILS.PRODUCT_STOCK_DETAILS_ID.eq(PRODUCT_STOCK_DETAILS.ID))
            .innerJoin(PRODUCTS).on(PRODUCT_STOCK_DETAILS.PRODUCT_ID.eq(PRODUCTS.ID))
            .where(ORDER_DETAILS.ID.eq(orderId))
            .fetch();
    }
}
