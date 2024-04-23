package com.militarystore.port.in.product;

import com.militarystore.entity.product.ProductFeedback;

import java.util.List;

public interface ProductFeedbackUseCase {

    Integer saveFeedback(ProductFeedback productFeedback);

    void updateFeedback(ProductFeedback productFeedback);

    void deleteFeedback(Integer feedbackId, Integer userId);

    ProductFeedback getFeedbackById(Integer feedbackId);

    List<ProductFeedback> getFeedbacksByProductId(Integer productId);
}
