package com.militarystore.basket.mapper;

import com.militarystore.entity.basket.ProductInBasket;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.militarystore.jooq.Tables.BASKETS;
import static com.militarystore.jooq.Tables.PRODUCTS;

@Component
public class BasketMapper implements RecordMapper<Record, ProductInBasket> {

    @Override
    public ProductInBasket map(Record productInBasketRecord) {
        return ProductInBasket.builder()
            .productId(productInBasketRecord.get(PRODUCTS.ID))
            .productName(productInBasketRecord.get(PRODUCTS.NAME))
            .productPrice(productInBasketRecord.get(PRODUCTS.PRICE))
            .quantity(productInBasketRecord.get(BASKETS.QUANTITY))
            .build();
    }
}
