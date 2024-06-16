package com.militarystore.product;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.ProductDetails;
import com.militarystore.entity.product.ProductFeedback;
import com.militarystore.entity.product.ProductStockDetails;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.port.in.product.ProductUseCase;
import com.militarystore.port.out.googledrive.GoogleDrivePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.militarystore.jooq.Tables.BASKETS;
import static com.militarystore.jooq.Tables.CATEGORIES;
import static com.militarystore.jooq.Tables.IMAGES;
import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.PRODUCT_FEEDBACKS;
import static com.militarystore.jooq.Tables.PRODUCT_RATES;
import static com.militarystore.jooq.Tables.PRODUCT_STOCK_DETAILS;
import static com.militarystore.jooq.Tables.SUBCATEGORIES;
import static com.militarystore.jooq.Tables.USERS;
import static com.militarystore.jooq.Tables.WISHLISTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

class ProductIntegrationTest extends IntegrationTest {

    private static final int SUBCATEGORY_ID = 1;
    private static final int PRODUCT_ID = 10;
    private static final int PRODUCT_STOCK_DETAILS_ID = 11;
    private static final int USER_ID = 1;
    private static final String GOOGLE_DRIVE_FILE_ID = "fileId";

    @Autowired
    private ProductUseCase productUseCase;

    @MockBean
    private GoogleDrivePort googleDrivePort;

    @Test
    void addProduct_shouldCorrectStoreProduct() {
        initializeCategories();

        var multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
        var image = new byte[]{1, 2, 3};
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

        when(googleDrivePort.uploadFiles(List.of(multipartFile))).thenReturn(List.of(GOOGLE_DRIVE_FILE_ID));
        when(googleDrivePort.downloadFiles(List.of(GOOGLE_DRIVE_FILE_ID))).thenReturn(List.of(image));

        var productId = productUseCase.addProduct(product, List.of(multipartFile));
        var productFromDb = productUseCase.getProductById(productId, USER_ID);

        assertThat(productFromDb.product().name()).isEqualTo(product.name());
        assertThat(productFromDb.product().description()).isEqualTo(product.description());
        assertThat(productFromDb.product().price()).isEqualTo(product.price());
        assertThat(productFromDb.product().stockDetails().get(0).productSize()).isEqualTo(product.stockDetails().get(0).productSize());
        assertThat(productFromDb.product().stockDetails().get(0).stockAvailability()).isEqualTo(product.stockDetails().get(0).stockAvailability());
        assertThat(productFromDb.images()).isEqualTo(List.of(image));
    }

    @Test
    void getProductById_shouldReturnCorrectProduct() {
        initializeCategories();
        initializeProduct();
        initializeImages();

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
            .avgRate(0.0)
            .feedbacks(List.of())
            .isProductInUserWishlist(false)
            .build();
        var images = List.of(new byte[]{1, 2, 3});
        var expectedResults = ProductDetails.builder()
            .product(product)
            .images(images)
            .build();

        when(googleDrivePort.downloadFiles(List.of(GOOGLE_DRIVE_FILE_ID, "googleDriveId2"))).thenReturn(images);

        var productFromDb = productUseCase.getProductById(PRODUCT_ID, USER_ID);

        assertThat(productFromDb).isEqualTo(expectedResults);
    }

    @Test
    void getProductById_shouldReturnCorrectProduct_whenProductHasAvgRate() {
        initializeCategories();
        initializeProduct();
        initializeUser();
        initializeProductRates();
        initializeImages();

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
            .avgRate(4.5)
            .feedbacks(List.of())
            .isProductInUserWishlist(false)
            .build();
        var images = List.of(new byte[]{1, 2, 3});
        var expectedResults = ProductDetails.builder()
            .product(product)
            .images(images)
            .build();

        when(googleDrivePort.downloadFiles(List.of(GOOGLE_DRIVE_FILE_ID, "googleDriveId2"))).thenReturn(images);

        var productFromDb = productUseCase.getProductById(PRODUCT_ID, USER_ID);

        assertThat(productFromDb).isEqualTo(expectedResults);
    }

    @Test
    void getProductById_shouldReturnCorrectProduct_whenProductHasAvgRateAndFeedbacks() {
        initializeCategories();
        initializeProduct();
        initializeUser();
        initializeProductRates();
        initializeFeedbacks();
        initializeImages();

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
            .avgRate(4.5)
            .feedbacks(List.of(ProductFeedback.builder()
                .id(1)
                .userId(1)
                .userLogin("login")
                .feedback("Feedback")
                .dateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build()))
            .isProductInUserWishlist(false)
            .build();

        var images = List.of(new byte[]{1, 2, 3});
        var expectedResults = ProductDetails.builder()
            .product(product)
            .images(images)
            .build();

        when(googleDrivePort.downloadFiles(List.of(GOOGLE_DRIVE_FILE_ID, "googleDriveId2"))).thenReturn(images);

        var productFromDb = productUseCase.getProductById(PRODUCT_ID, USER_ID);

        assertThat(productFromDb).isEqualTo(expectedResults);
    }

    @Test
    void getProductById_shouldReturnCorrectProduct_whenProductIsInUserWishlist() {
        initializeCategories();
        initializeProduct();
        initializeUser();
        initializeWishlist();
        initializeImages();

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
            .avgRate(0.0)
            .feedbacks(List.of())
            .isProductInUserWishlist(true)
            .build();
        var images = List.of(new byte[]{1, 2, 3});
        var expectedResults = ProductDetails.builder()
            .product(product)
            .images(images)
            .build();

        when(googleDrivePort.downloadFiles(List.of(GOOGLE_DRIVE_FILE_ID, "googleDriveId2"))).thenReturn(images);

        var productFromDb = productUseCase.getProductById(PRODUCT_ID, USER_ID);

        assertThat(productFromDb).isEqualTo(expectedResults);
    }

    @Test
    void getProductsBySubcategoryId_shouldReturnCorrectProducts() {
        initializeCategories();
        initializeProduct();
        initializeImages();

        var product = Product.builder()
            .id(PRODUCT_ID)
            .name("Product")
            .price(100)
            .tag(ProductTag.NEW)
            .isInStock(true)
            .build();
        var image = new byte[]{1, 2, 3};
        var expectedResults = ProductDetails.builder()
            .product(product)
            .images(List.of(image))
            .build();

        when(googleDrivePort.downloadFile(GOOGLE_DRIVE_FILE_ID)).thenReturn(image);

        var products = productUseCase.getProductsBySubcategoryId(SUBCATEGORY_ID);

        assertThat(products).isEqualTo(List.of(expectedResults));
    }

    @Test
    void getProductsByName_shouldReturnCorrectProducts() {
        initializeCategories();
        initializeProduct();
        initializeImages();

        var product = Product.builder()
            .id(PRODUCT_ID)
            .name("Product")
            .price(100)
            .tag(ProductTag.NEW)
            .isInStock(true)
            .build();
        var image = new byte[]{1, 2, 3};
        var expectedResults = ProductDetails.builder()
            .product(product)
            .images(List.of(image))
            .build();

        when(googleDrivePort.downloadFile(GOOGLE_DRIVE_FILE_ID)).thenReturn(image);

        var products = productUseCase.getProductsByName("Product");

        assertThat(products).isEqualTo(List.of(expectedResults));
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
            .avgRate(0.0)
            .feedbacks(List.of())
            .isProductInUserWishlist(false)
            .build();

        productUseCase.updateProduct(productToUpdate);

        var productFromDb = productUseCase.getProductById(PRODUCT_ID, USER_ID).product();

        assertThat(productFromDb).isEqualTo(productToUpdate);
    }

    @Test
    void deleteProduct_shouldCorrectlyDeleteProduct() {
        initializeCategories();
        initializeProduct();

        productUseCase.deleteProduct(PRODUCT_ID);

        assertThat(productUseCase.getProductsBySubcategoryId(SUBCATEGORY_ID)).isEmpty();
    }

    @Test
    void deleteProductWithAllRelations() {
        initializeCategories();
        initializeProduct();
        initializeUser();
        initializeProductRates();
        initializeFeedbacks();
        initializeWishlist();
        initializeBasket();

        assertDoesNotThrow(() -> productUseCase.deleteProduct(PRODUCT_ID));
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

    private void initializeProductRates() {
        dslContext.insertInto(PRODUCT_RATES, PRODUCT_RATES.PRODUCT_ID, PRODUCT_RATES.USER_ID, PRODUCT_RATES.RATE)
            .values(PRODUCT_ID, 1, 5.0)
            .values(PRODUCT_ID, 2, 4.0)
            .execute();
    }

    private void initializeUser() {
        dslContext.insertInto(USERS,
                USERS.ID,
                USERS.LOGIN,
                USERS.PASSWORD,
                USERS.FIRST_NAME,
                USERS.SECOND_NAME,
                USERS.EMAIL,
                USERS.PHONE,
                USERS.GENDER,
                USERS.BIRTHDAY_DATE,
                USERS.ROLE,
                USERS.IS_BANNED)
            .values(USER_ID, "login", "password", "firstName", "secondName", "email", "phone", "gender", LocalDate.EPOCH, "role", true)
            .values(2, "login2", "password2", "firstName2", "secondName2", "email2", "phone2", "gender2", LocalDate.EPOCH, "role2", false)
            .execute();
    }

    private void initializeFeedbacks() {
        dslContext.insertInto(PRODUCT_FEEDBACKS)
            .set(PRODUCT_FEEDBACKS.ID, 1)
            .set(PRODUCT_FEEDBACKS.PRODUCT_ID, PRODUCT_ID)
            .set(PRODUCT_FEEDBACKS.USER_ID, USER_ID)
            .set(PRODUCT_FEEDBACKS.FEEDBACK, "Feedback")
            .set(PRODUCT_FEEDBACKS.DATE_TIME, LocalDateTime.of(2021, 1, 1, 0, 0))
            .execute();
    }

    private void initializeWishlist() {
        dslContext.insertInto(WISHLISTS)
            .set(WISHLISTS.ID, 1)
            .set(WISHLISTS.USER_ID, USER_ID)
            .set(WISHLISTS.PRODUCT_ID, PRODUCT_ID)
            .execute();
    }

    private void initializeBasket() {
        dslContext.insertInto(BASKETS)
            .set(BASKETS.PRODUCT_STOCK_DETAILS_ID, PRODUCT_STOCK_DETAILS_ID)
            .set(BASKETS.USER_ID, USER_ID)
            .set(BASKETS.QUANTITY, 1)
            .execute();
    }

    private void initializeImages() {
        dslContext.insertInto(IMAGES,
                IMAGES.PRODUCT_ID,
                IMAGES.GOOGLE_DRIVE_ID,
                IMAGES.ORDINAL_NUMBER
            )
            .values(PRODUCT_ID, GOOGLE_DRIVE_FILE_ID, 1)
            .values(PRODUCT_ID, "googleDriveId2", 2)
            .execute();
    }
}
