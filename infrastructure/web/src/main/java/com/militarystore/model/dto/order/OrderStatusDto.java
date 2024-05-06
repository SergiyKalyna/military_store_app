package com.militarystore.model.dto.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatusDto {
    NEW("New"),
    IN_PROGRESS("In Progress"),
    SHIPPED("Shipped"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String value;
}
