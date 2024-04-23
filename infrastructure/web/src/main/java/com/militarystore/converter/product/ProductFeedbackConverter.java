package com.militarystore.converter.product;

import com.militarystore.entity.product.ProductFeedback;
import com.militarystore.model.dto.product.ProductFeedbackDto;
import com.militarystore.model.request.product.ProductFeedbackRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductFeedbackConverter {

    public ProductFeedback convertToProductFeedback(ProductFeedbackRequest productFeedbackRequest, Integer userId) {
        return ProductFeedback.builder()
            .userId(userId)
            .productId(productFeedbackRequest.productId())
            .feedback(productFeedbackRequest.feedback())
            .dateTime(LocalDateTime.now())
            .build();
    }

    public ProductFeedback convertToProductFeedback(
        ProductFeedbackRequest productFeedbackRequest,
        Integer feedbackId,
        Integer userId
    ) {
        return ProductFeedback.builder()
            .id(feedbackId)
            .userId(userId)
            .productId(productFeedbackRequest.productId())
            .feedback(productFeedbackRequest.feedback())
            .dateTime(LocalDateTime.now())
            .build();
    }

    public ProductFeedbackDto convertToProductFeedbackDto(ProductFeedback productFeedback) {
        return ProductFeedbackDto.builder()
            .id(productFeedback.id())
            .userId(productFeedback.userId())
            .userLogin(productFeedback.userLogin())
            .feedback(productFeedback.feedback())
            .dateTime(productFeedback.dateTime())
            .build();
    }
}
