package com.militarystore.model.request.product;

public record ProductFeedbackRequest(
    Integer productId,
    String feedback
) {
}
