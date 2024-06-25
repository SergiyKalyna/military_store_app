package com.militarystore.order;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.delivery.DeliveryDetails;
import com.militarystore.entity.email.EmailDetails;
import com.militarystore.entity.order.Order;
import com.militarystore.entity.order.OrderDetails;
import com.militarystore.entity.order.OrderStatus;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.entity.user.model.Gender;
import com.militarystore.entity.user.model.Role;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.in.order.GetOrderUseCase;
import com.militarystore.port.in.order.SubmitOrderUseCase;
import com.militarystore.port.in.order.UpdateOrderUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.militarystore.jooq.Tables.CATEGORIES;
import static com.militarystore.jooq.Tables.DISCOUNTS;
import static com.militarystore.jooq.Tables.ORDERS;
import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.PRODUCT_STOCK_DETAILS;
import static com.militarystore.jooq.Tables.SUBCATEGORIES;
import static com.militarystore.jooq.Tables.USERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class OrderIntegrationTest extends IntegrationTest {

    private static final int SUBCATEGORY_ID = 1;
    private static final int PRODUCT_ID = 10;
    private static final int PRODUCT_STOCK_DETAILS_ID = 11;
    private static final int USER_ID = 111;
    private static final int ORDER_ID = 2;
    private static final String DISCOUNT_CODE = "code";

    @Autowired
    private SubmitOrderUseCase submitOrderUseCase;

    @Autowired
    private GetOrderUseCase getOrderUseCase;

    @Autowired
    private UpdateOrderUseCase updateOrderUseCase;

    @Test
    void submitOrder_whenDeliveryDetailsIsNotValid_shouldThrowException() {
        initializeCategories();
        initializeProduct();
        initializeUser();

        var order = Order.builder()
            .userId(USER_ID)
            .orderDetails(List.of(OrderDetails.builder()
                .productId(PRODUCT_ID)
                .productName("Product")
                .productStockDetailsId(PRODUCT_STOCK_DETAILS_ID)
                .quantity(11)
                .build()))
            .deliveryDetails(DeliveryDetails.builder().city("Kharkiv").postNumber(11).build())
            .build();

        assertThrows(MsValidationException.class, () -> submitOrderUseCase.submitOrder(order, null));
        verifyNoInteractions(sendEmailPort);
    }

    @Test
    void submitOrder_whenProductIsNotInStock_shouldThrowException() {
        initializeCategories();
        initializeProduct();
        initializeUser();

        var order = Order.builder()
            .userId(USER_ID)
            .orderDetails(List.of(OrderDetails.builder()
                .productId(PRODUCT_ID)
                .productName("Product")
                .productStockDetailsId(PRODUCT_STOCK_DETAILS_ID)
                .quantity(11)
                .build()))
            .build();

        assertThrows(MsValidationException.class, () -> submitOrderUseCase.submitOrder(order, null));
        verifyNoInteractions(sendEmailPort);
    }

    @Test
    void submitOrder_withoutDiscountCode() {
        initializeCategories();
        initializeProduct();
        initializeUser();

        var order = Order.builder()
            .userId(USER_ID)
            .deliveryDetails(
                DeliveryDetails.builder()
                    .city("Kharkiv")
                    .postNumber(11)
                    .recipientName("Name")
                    .recipientPhone("+380973214376")
                    .build()
            )
            .orderDetails(List.of(OrderDetails.builder()
                .productId(PRODUCT_ID)
                .productName("Product")
                .productStockDetailsId(PRODUCT_STOCK_DETAILS_ID)
                .quantity(1)
                .build()))
            .orderDate(LocalDate.now())
            .status(OrderStatus.NEW)
            .totalAmount(100)
            .build();

        var orderId = submitOrderUseCase.submitOrder(order, null);
        var orderFromDb = getOrderUseCase.getOrderById(orderId);

        var expectedOrder = Order.builder()
            .id(orderId)
            .userId(USER_ID)
            .deliveryDetails(
                DeliveryDetails.builder()
                    .city("Kharkiv")
                    .postNumber(11)
                    .recipientName("Name")
                    .recipientPhone("+380973214376")
                    .build()
            )
            .orderDetails(List.of(OrderDetails.builder()
                .productId(PRODUCT_ID)
                .productName("Product")
                .productStockDetailsId(PRODUCT_STOCK_DETAILS_ID)
                .quantity(1)
                .productSize(ProductSize.M)
                .productPrice(100)
                .build()))
            .orderDate(LocalDate.now())
            .status(OrderStatus.NEW)
            .totalAmount(100)
            .build();

        var emailDetails = EmailDetails.builder()
            .message("your order has been submitted successfully, your order id is - " + orderId)
            .recipientEmail("email")
            .subject("Military Store Order Details")
            .username("firstName secondName")
            .build();

        assertEquals(expectedOrder, orderFromDb);
        assertThat(getProductStockDetailsQuantity()).isEqualTo(9);
        verify(sendEmailPort).sendEmail(emailDetails);
    }

    @Test
    void submitOrder_withDiscountCode() {
        initializeCategories();
        initializeProduct();
        initializeUser();
        initializeDiscount();

        var order = Order.builder()
            .userId(USER_ID)
            .deliveryDetails(
                DeliveryDetails.builder()
                    .city("Kharkiv")
                    .postNumber(11)
                    .recipientName("Name")
                    .recipientPhone("+380973214376")
                    .build()
            )
            .orderDetails(List.of(OrderDetails.builder()
                .productId(PRODUCT_ID)
                .productStockDetailsId(PRODUCT_STOCK_DETAILS_ID)
                .quantity(1)
                .build()))
            .orderDate(LocalDate.now())
            .status(OrderStatus.NEW)
            .totalAmount(100)
            .discount(0.03)
            .build();

        var orderId = submitOrderUseCase.submitOrder(order, DISCOUNT_CODE);
        var orderFromDb = getOrderUseCase.getOrderById(orderId);

        var expectedOrder = Order.builder()
            .id(orderId)
            .userId(USER_ID)
            .deliveryDetails(
                DeliveryDetails.builder()
                    .city("Kharkiv")
                    .postNumber(11)
                    .recipientName("Name")
                    .recipientPhone("+380973214376")
                    .build()
            )
            .orderDetails(List.of(OrderDetails.builder()
                .productId(PRODUCT_ID)
                .productName("Product")
                .productStockDetailsId(PRODUCT_STOCK_DETAILS_ID)
                .quantity(1)
                .productSize(ProductSize.M)
                .productPrice(100)
                .build()))
            .orderDate(LocalDate.now())
            .status(OrderStatus.NEW)
            .totalAmount(100)
            .discount(0.03)
            .build();

        var emailDetails = EmailDetails.builder()
            .message("your order has been submitted successfully, your order id is - " + orderId)
            .recipientEmail("email")
            .subject("Military Store Order Details")
            .username("firstName secondName")
            .build();

        assertEquals(expectedOrder, orderFromDb);
        assertThat(getProductStockDetailsQuantity()).isEqualTo(9);
        assertThat(getDiscountUsageLimit()).isZero();
        verify(sendEmailPort).sendEmail(emailDetails);
    }

    @Test
    void updateOrderStatus() {
        initializeCategories();
        initializeProduct();
        initializeUser();
        initializeOrder();

        updateOrderUseCase.updateOrderStatus(ORDER_ID, OrderStatus.IN_PROGRESS);

        var ordersFromDb = getOrderUseCase.getUserOrders(USER_ID);
        var expectedResult = List.of(
            Order.builder()
                .id(ORDER_ID)
                .orderDate(LocalDate.now())
                .status(OrderStatus.IN_PROGRESS)
                .totalAmount(100)
                .build()
        );

        var emailDetails = EmailDetails.builder()
            .message("your order with id #2 has changed status to 'In progress'")
            .recipientEmail("email")
            .subject("Military Store Order Details")
            .username("firstName secondName")
            .build();

        assertThat(ordersFromDb).isEqualTo(expectedResult);
        verify(sendEmailPort).sendEmail(emailDetails);
    }

    @Test
    void updateOrderStatusWithShippingNumber() {
        initializeCategories();
        initializeProduct();
        initializeUser();
        initializeOrder();

        var shippingNumber = "shippingNumber";
        updateOrderUseCase.updateOrderStatusWithShippingNumber(ORDER_ID, OrderStatus.SHIPPED, shippingNumber);

        var ordersFromDb = getOrderUseCase.getOrdersByStatus(OrderStatus.SHIPPED);
        var expectedResult = List.of(
            Order.builder()
                .id(ORDER_ID)
                .orderDate(LocalDate.now())
                .status(OrderStatus.SHIPPED)
                .totalAmount(100)
                .shippingNumber(shippingNumber)
                .build()
        );
        var emailDetails = EmailDetails.builder()
            .message("your order with id #2 has changed status to 'Shipped', your shipping number - shippingNumber")
            .recipientEmail("email")
            .subject("Military Store Order Details")
            .username("firstName secondName")
            .build();

        assertThat(ordersFromDb).isEqualTo(expectedResult);
        assertThat(getOrderUseCase.getOrdersByStatus(OrderStatus.NEW)).isEmpty();
        verify(sendEmailPort).sendEmail(emailDetails);
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

    private void initializeUser() {
        dslContext.insertInto(USERS)
            .set(USERS.ID, USER_ID)
            .set(USERS.LOGIN, "login")
            .set(USERS.PASSWORD, "password")
            .set(USERS.EMAIL, "email")
            .set(USERS.FIRST_NAME, "firstName")
            .set(USERS.SECOND_NAME, "secondName")
            .set(USERS.PHONE, "+380935334711")
            .set(USERS.BIRTHDAY_DATE, LocalDate.EPOCH)
            .set(USERS.ROLE, Role.USER.name())
            .set(USERS.GENDER, Gender.MALE.name())
            .set(USERS.IS_BANNED, false)
            .execute();
    }

    private void initializeDiscount() {
        dslContext.insertInto(DISCOUNTS)
            .set(DISCOUNTS.ID, 1)
            .set(DISCOUNTS.USER_ID, USER_ID)
            .set(DISCOUNTS.DISCOUNT_CODE, DISCOUNT_CODE)
            .set(DISCOUNTS.DISCOUNT, 0.03)
            .set(DISCOUNTS.USAGE_LIMIT, 1)
            .set(DISCOUNTS.EXPIRATION_DATE, LocalDateTime.now().plusDays(1))
            .execute();
    }

    private void initializeOrder() {
        dslContext.insertInto(ORDERS)
            .set(ORDERS.ID, ORDER_ID)
            .set(ORDERS.USER_ID, USER_ID)
            .set(ORDERS.DELIVERY_DETAILS_ID, 1)
            .set(ORDERS.ORDER_DATE, LocalDate.now())
            .set(ORDERS.ORDER_STATUS, OrderStatus.NEW.name())
            .set(ORDERS.TOTAL_AMOUNT, 100)
            .execute();
    }

    private Integer getProductStockDetailsQuantity() {
        return dslContext.select(PRODUCT_STOCK_DETAILS.STOCK_AVAILABILITY)
            .from(PRODUCT_STOCK_DETAILS)
            .where(PRODUCT_STOCK_DETAILS.ID.eq(PRODUCT_STOCK_DETAILS_ID))
            .fetchOne(PRODUCT_STOCK_DETAILS.STOCK_AVAILABILITY);
    }

    private Integer getDiscountUsageLimit() {
        return dslContext.select(DISCOUNTS.USAGE_LIMIT)
            .from(DISCOUNTS)
            .where(DISCOUNTS.DISCOUNT_CODE.eq(DISCOUNT_CODE))
            .fetchOne(DISCOUNTS.USAGE_LIMIT);
    }

    private void assertEquals(Order expected, Order actual) {
        assertThat(actual.discount()).isEqualTo(expected.discount());
        assertThat(actual.totalAmount()).isEqualTo(expected.totalAmount());
        assertThat(actual.orderDate()).isEqualTo(expected.orderDate());
        assertThat(actual.status()).isEqualTo(expected.status());

        assertThat(actual.deliveryDetails().city()).isEqualTo(expected.deliveryDetails().city());
        assertThat(actual.deliveryDetails().postNumber()).isEqualTo(expected.deliveryDetails().postNumber());
        assertThat(actual.deliveryDetails().recipientName()).isEqualTo(expected.deliveryDetails().recipientName());
        assertThat(actual.deliveryDetails().recipientPhone()).isEqualTo(expected.deliveryDetails().recipientPhone());

        var actualOrderDetails = actual.orderDetails().get(0);
        var expectedOrderDetails = expected.orderDetails().get(0);

        assertThat(actualOrderDetails.productId()).isEqualTo(expectedOrderDetails.productId());
        assertThat(actualOrderDetails.productStockDetailsId()).isEqualTo(expectedOrderDetails.productStockDetailsId());
        assertThat(actualOrderDetails.productName()).isEqualTo(expectedOrderDetails.productName());
        assertThat(actualOrderDetails.quantity()).isEqualTo(expectedOrderDetails.quantity());
        assertThat(actualOrderDetails.productSize()).isEqualTo(expectedOrderDetails.productSize());
        assertThat(actualOrderDetails.productPrice()).isEqualTo(expectedOrderDetails.productPrice());
    }
}
