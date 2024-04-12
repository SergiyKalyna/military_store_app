package com.militarystore.product;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.ProductStockDetails;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.port.in.product.ProductUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.militarystore.jooq.Tables.CATEGORIES;
import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.PRODUCT_STOCK_DETAILS;
import static com.militarystore.jooq.Tables.SUBCATEGORIES;
import static org.assertj.core.api.Assertions.assertThat;

class ProductIntegrationTest extends IntegrationTest {

    private static final int SUBCATEGORY_ID = 1;
    private static final int PRODUCT_ID = 10;
    private static final int PRODUCT_STOCK_DETAILS_ID = 11;

    @Autowired
    private ProductUseCase productUseCase;

    @Test
    void addProduct_shouldCorrectStoreProduct() {
        initializeCategories();

        var product = Product.builder()
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(SUBCATEGORY_ID)
            .sizeGridType(ProductSizeGridType.CLOTHES)
            .tag(ProductTag.NEW)
            .stockDetails(List.of(
                ProductStockDetails.builder()
                    .productSize(ProductSize.M)
                    .stockAvailability(10)
                    .build()))
            .build();

        var productId = productUseCase.addProduct(product);
        var productFromDb = productUseCase.getProductById(productId);

        assertThat(productFromDb.name()).isEqualTo(product.name());
        assertThat(productFromDb.description()).isEqualTo(product.description());
        assertThat(productFromDb.price()).isEqualTo(product.price());
        assertThat(productFromDb.stockDetails().get(0).productSize()).isEqualTo(product.stockDetails().get(0).productSize());
        assertThat(productFromDb.stockDetails().get(0).stockAvailability()).isEqualTo(product.stockDetails().get(0).stockAvailability());
    }

    @Test
    void getProductById_shouldReturnCorrectProduct() {
        initializeCategories();
        initializeProduct();

        var product = Product.builder()
            .id(PRODUCT_ID)
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(SUBCATEGORY_ID)
            .sizeGridType(ProductSizeGridType.CLOTHES)
            .tag(ProductTag.NEW)
            .stockDetails(List.of(
                ProductStockDetails.builder()
                    .id(PRODUCT_STOCK_DETAILS_ID)
                    .productId(PRODUCT_ID)
                    .productSize(ProductSize.M)
                    .stockAvailability(10)
                    .build()))
            .isInStock(true)
            .build();

        var productFromDb = productUseCase.getProductById(PRODUCT_ID);

        assertThat(productFromDb).isEqualTo(product);
    }

    @Test
    void getProductsBySubcategoryId_shouldReturnCorrectProducts() {
        initializeCategories();
        initializeProduct();

        var product = Product.builder()
            .id(PRODUCT_ID)
            .name("Product")
            .price(100)
            .tag(ProductTag.NEW)
            .isInStock(true)
            .build();

        var products = productUseCase.getProductsBySubcategoryId(SUBCATEGORY_ID);

        assertThat(products).isEqualTo(List.of(product));
    }

    @Test
    void getProductsByName_shouldReturnCorrectProducts() {
        initializeCategories();
        initializeProduct();

        var product = Product.builder()
            .id(PRODUCT_ID)
            .name("Product")
            .price(100)
            .tag(ProductTag.NEW)
            .isInStock(true)
            .build();

        var products = productUseCase.getProductsByName("Product");

        assertThat(products).isEqualTo(List.of(product));
    }

    @Test
    void updateProduct_shouldCorrectlyUpdateProduct() {
        initializeCategories();
        initializeProduct();

        var productToUpdate = Product.builder()
            .id(PRODUCT_ID)
            .name("Updated product")
            .description("Updated product description")
            .price(200)
            .subcategoryId(SUBCATEGORY_ID)
            .sizeGridType(ProductSizeGridType.SHOES)
            .tag(ProductTag.SALE)
            .stockDetails(List.of(
                ProductStockDetails.builder()
                    .id(PRODUCT_STOCK_DETAILS_ID)
                    .productId(PRODUCT_ID)
                    .productSize(ProductSize.L)
                    .stockAvailability(9)
                    .build()))
            .isInStock(true)
            .build();

        productUseCase.updateProduct(productToUpdate);

        var productFromDb = productUseCase.getProductById(PRODUCT_ID);

        assertThat(productFromDb).isEqualTo(productToUpdate);
    }

    @Test
    void deleteProduct_shouldCorrectlyDeleteProduct() {
        initializeCategories();
        initializeProduct();

        productUseCase.deleteProduct(PRODUCT_ID);

        assertThat(productUseCase.getProductsBySubcategoryId(SUBCATEGORY_ID)).isEmpty();
    }

    private void initializeCategories() {
       dslContext.insertInto(CATEGORIES)
           .set(CATEGORIES.ID, 1)
           .set(CATEGORIES.NAME, "Category")
           .execute();

       dslContext.insertInto(SUBCATEGORIES)
           .set(SUBCATEGORIES.ID, SUBCATEGORY_ID)
           .set(SUBCATEGORIES.NAME, "Subcategory")
           .set(SUBCATEGORIES.CATEGORY_ID, 1)
           .execute();
    }

    private void initializeProduct() {
        dslContext.insertInto(PRODUCTS)
            .set(PRODUCTS.ID, PRODUCT_ID)
            .set(PRODUCTS.NAME, "Product")
            .set(PRODUCTS.DESCRIPTION, "Product description")
            .set(PRODUCTS.PRICE, 100)
            .set(PRODUCTS.SUBCATEGORY_ID, SUBCATEGORY_ID)
            .set(PRODUCTS.SIZE_GRID_TYPE, ProductSizeGridType.CLOTHES.name())
            .set(PRODUCTS.PRODUCT_TAG, ProductTag.NEW.name())
            .set(PRODUCTS.IS_IN_STOCK, true)
            .execute();

        dslContext.insertInto(PRODUCT_STOCK_DETAILS)
            .set(PRODUCT_STOCK_DETAILS.ID, PRODUCT_STOCK_DETAILS_ID)
            .set(PRODUCT_STOCK_DETAILS.PRODUCT_ID, PRODUCT_ID)
            .set(PRODUCT_STOCK_DETAILS.PRODUCT_SIZE, ProductSize.M.name())
            .set(PRODUCT_STOCK_DETAILS.STOCK_AVAILABILITY, 10)
            .execute();
    }
}
