package com.militarystore.product;

import com.militarystore.entity.product.Product;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.PRODUCT_STOCK_DETAILS;
import static org.apache.commons.lang3.ArrayUtils.addAll;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final DSLContext dslContext;

    public Integer addProduct(Product product) {
        return dslContext.insertInto(PRODUCTS)
            .set(PRODUCTS.NAME, product.name())
            .set(PRODUCTS.DESCRIPTION, product.description())
            .set(PRODUCTS.PRICE, product.price())
            .set(PRODUCTS.SUBCATEGORY_ID, product.subcategoryId())
            .set(PRODUCTS.SIZE_GRID_TYPE, product.sizeGridType().name())
            .set(PRODUCTS.PRODUCT_TAG, product.tag().name())
            .returning(PRODUCTS.ID)
            .fetchOne(PRODUCTS.ID);
    }

    public List<Record> getProductById(Integer productId) {
        return dslContext.select(addAll(PRODUCTS.fields(), PRODUCT_STOCK_DETAILS.fields()))
            .from(PRODUCTS)
            .innerJoin(PRODUCT_STOCK_DETAILS).on(PRODUCTS.ID.eq(PRODUCT_STOCK_DETAILS.PRODUCT_ID))
            .where(PRODUCTS.ID.eq(productId))
            .fetchInto(Record.class);
    }

    public List<Record> getProductsBySubcategoryId(Integer subcategoryId) {
        return dslContext.select(
                PRODUCTS.ID,
                PRODUCTS.NAME,
                PRODUCTS.PRICE,
                PRODUCTS.PRODUCT_TAG,
                PRODUCTS.IS_IN_STOCK
            )
            .from(PRODUCTS)
            .where(PRODUCTS.SUBCATEGORY_ID.eq(subcategoryId))
            .fetchInto(Record.class);
    }

    public List<Record> getProductsByName(String name) {
        return dslContext.select(
                PRODUCTS.ID,
                PRODUCTS.NAME,
                PRODUCTS.PRICE,
                PRODUCTS.PRODUCT_TAG,
                PRODUCTS.IS_IN_STOCK
            )
            .from(PRODUCTS)
            .where(PRODUCTS.NAME.containsIgnoreCase(name))
            .fetchInto(Record.class);
    }

    public void updateProduct(Product product) {
        dslContext.update(PRODUCTS)
            .set(PRODUCTS.NAME, product.name())
            .set(PRODUCTS.DESCRIPTION, product.description())
            .set(PRODUCTS.PRICE, product.price())
            .set(PRODUCTS.SUBCATEGORY_ID, product.subcategoryId())
            .set(PRODUCTS.SIZE_GRID_TYPE, product.sizeGridType().name())
            .set(PRODUCTS.PRODUCT_TAG, product.tag().name())
            .where(PRODUCTS.ID.eq(product.id()))
            .execute();
    }

    public void updateStockAvailability(Integer productId, boolean isInStock) {
        dslContext.update(PRODUCTS)
            .set(PRODUCTS.IS_IN_STOCK, isInStock)
            .where(PRODUCTS.ID.eq(productId))
            .execute();
    }

    public void deleteProduct(Integer productId) {
        dslContext.deleteFrom(PRODUCTS)
            .where(PRODUCTS.ID.eq(productId))
            .execute();
    }

    public boolean isProductExist(Integer productId) {
        return dslContext.fetchExists(dslContext.selectFrom(PRODUCTS)
            .where(PRODUCTS.ID.eq(productId))
        );
    }
}
