package com.militarystore.port.out.product;

import com.militarystore.entity.product.ProductFeedback;

import java.util.List;

public interface ProductFeedbackPort {

    Integer saveFeedback(ProductFeedback productFeedback);

    void updateFeedback(ProductFeedback productFeedback);

    void deleteFeedback(Integer feedbackId);

    void deleteFeedbacksByProductId(Integer productId);

    ProductFeedback getFeedbackById(Integer feedbackId);

    List<ProductFeedback> getFeedbacksByProductId(Integer productId);

    boolean isFeedbackExist(Integer feedbackId);

    boolean canUserChangeFeedback(Integer feedbackId, Integer userId);
}
