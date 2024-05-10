package com.militarystore.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.militarystore.config.TestSecurityConfig;
import com.militarystore.converter.order.OrderConverter;
import com.militarystore.entity.order.Order;
import com.militarystore.entity.order.OrderStatus;
import com.militarystore.model.dto.order.OrderDto;
import com.militarystore.model.dto.order.OrderStatusDto;
import com.militarystore.model.request.order.SubmitOrderRequest;
import com.militarystore.model.response.order.GetOrderResponse;
import com.militarystore.port.in.order.GetOrderUseCase;
import com.militarystore.port.in.order.SubmitOrderUseCase;
import com.militarystore.port.in.order.UpdateOrderUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@ContextConfiguration(classes = OrderController.class)
@Import(TestSecurityConfig.class)
class OrderControllerTest {

    private static final Integer USER_ID = 1;
    private static final Integer ORDER_ID = 2;

    @MockBean
    private SubmitOrderUseCase submitOrderUseCase;

    @MockBean
    private GetOrderUseCase getOrderUseCase;

    @MockBean
    private UpdateOrderUseCase updateOrderUseCase;

    @MockBean
    private OrderConverter orderConverter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void submitOrder() throws Exception {
        var submitOrderRequest = SubmitOrderRequest.builder().build();
        var order = Order.builder().build();

        when(orderConverter.convertToOrder(submitOrderRequest, USER_ID)).thenReturn(order);
        when(submitOrderUseCase.submitOrder(order, submitOrderRequest.discountCode())).thenReturn(ORDER_ID);

        mockMvc.perform(post("/orders/user/{userId}", USER_ID)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(submitOrderRequest)))
            .andExpect(status().isOk())
            .andExpect(content().string(ORDER_ID.toString()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateOrderStatus_whenUserHasRoleAdmin_shouldUpdateOrderStatus() throws Exception {
        when(orderConverter.convertToOrderStatus(OrderStatusDto.COMPLETED)).thenReturn(OrderStatus.COMPLETED);

        mockMvc.perform(put("/orders/{orderId}", ORDER_ID)
                .param("status", OrderStatusDto.COMPLETED.name()))
            .andExpect(status().isOk());

        verify(updateOrderUseCase).updateOrderStatus(ORDER_ID, OrderStatus.COMPLETED);
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void updateOrderStatus_whenUserHasRoleSuperAdmin_shouldUpdateOrderStatus() throws Exception {
        when(orderConverter.convertToOrderStatus(OrderStatusDto.COMPLETED)).thenReturn(OrderStatus.COMPLETED);

        mockMvc.perform(put("/orders/{orderId}", ORDER_ID)
                .param("status", OrderStatusDto.COMPLETED.name()))
            .andExpect(status().isOk());

        verify(updateOrderUseCase).updateOrderStatus(ORDER_ID, OrderStatus.COMPLETED);
    }

    @Test
    @WithMockUser
    void updateOrderStatus_whenUserHasRoleUser_shouldReturnForbidden() throws Exception {
        mockMvc.perform(put("/orders/{orderId}", ORDER_ID)
                .param("status", OrderStatusDto.COMPLETED.name()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateOrderStatusWithShippingNumber_whenUserHasRoleAdmin_shouldUpdateOrderStatus() throws Exception {
        var shippingNumber = "shippingNumber";

        when(orderConverter.convertToOrderStatus(OrderStatusDto.SHIPPED)).thenReturn(OrderStatus.SHIPPED);

        mockMvc.perform(put("/orders/{orderId}/shipping", ORDER_ID)
                .param("status", OrderStatusDto.SHIPPED.name())
                .param("shippingNumber", shippingNumber))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void updateOrderStatusWithShippingNumber_whenUserHasRoleSuperAdmin_shouldUpdateOrderStatus() throws Exception {
        var shippingNumber = "shippingNumber";

        when(orderConverter.convertToOrderStatus(OrderStatusDto.SHIPPED)).thenReturn(OrderStatus.SHIPPED);

        mockMvc.perform(put("/orders/{orderId}/shipping", ORDER_ID)
                .param("status", OrderStatusDto.SHIPPED.name())
                .param("shippingNumber", shippingNumber))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void updateOrderStatusWithShippingNumber_whenUserHasRoleUser_shouldReturnForbidden() throws Exception {
        mockMvc.perform(put("/orders/{orderId}/shipping", ORDER_ID)
                .param("status", OrderStatusDto.SHIPPED.name())
                .param("shippingNumber", "shippingNumber"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void getUserOrders() throws Exception {
        var orders = List.of(Order.builder().id(1).userId(USER_ID).build());
        var orderDto = OrderDto.builder().id(1).build();

        when(orderConverter.convertToOrderDto(orders.get(0))).thenReturn(orderDto);
        when(getOrderUseCase.getUserOrders(USER_ID)).thenReturn(orders);


        mockMvc.perform(get("/orders/user/{userId}", USER_ID))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(List.of(orderDto))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOrdersByStatus_whenUserHasRoleAdmin_shouldReturnOrders() throws Exception {
        var status = OrderStatusDto.COMPLETED;
        var order = Order.builder().id(1).userId(USER_ID).build();
        var orderDto = OrderDto.builder().id(1).build();

        when(orderConverter.convertToOrderStatus(status)).thenReturn(OrderStatus.COMPLETED);
        when(getOrderUseCase.getOrdersByStatus(OrderStatus.COMPLETED)).thenReturn(List.of(order));
        when(orderConverter.convertToOrderDto(order)).thenReturn(orderDto);

        mockMvc.perform(get("/orders/status")
                .param("status", status.name()))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(List.of(orderDto))));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void getOrdersByStatus_whenUserHasRoleSuperAdmin_shouldReturnOrders() throws Exception {
        var status = OrderStatusDto.COMPLETED;
        var order = Order.builder().id(1).userId(USER_ID).build();
        var orderDto = OrderDto.builder().id(1).build();

        when(orderConverter.convertToOrderStatus(status)).thenReturn(OrderStatus.COMPLETED);
        when(getOrderUseCase.getOrdersByStatus(OrderStatus.COMPLETED)).thenReturn(List.of(order));
        when(orderConverter.convertToOrderDto(order)).thenReturn(orderDto);

        mockMvc.perform(get("/orders/status")
                .param("status", status.name()))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(List.of(orderDto))));
    }

    @Test
    @WithMockUser
    void getOrdersByStatus_whenUserHasRoleUser_shouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/orders/status")
                .param("status", OrderStatusDto.COMPLETED.name()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void getOrderById() throws Exception {
        var order = Order.builder().id(2).userId(USER_ID).build();
        var response = GetOrderResponse.builder().orderId(2).userId(USER_ID).build();

        when(getOrderUseCase.getOrderById(ORDER_ID)).thenReturn(order);
        when(orderConverter.convertToGetOrderResponse(order)).thenReturn(response);

        mockMvc.perform(get("/orders/{orderId}", ORDER_ID))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}