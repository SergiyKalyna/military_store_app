package com.militarystore.product.feedback;

import com.militarystore.entity.product.ProductFeedback;
import com.militarystore.port.out.product.ProductFeedbackPort;
import com.militarystore.product.mapper.ProductFeedbackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductFeedbackAdapter implements ProductFeedbackPort {

    private final ProductFeedbackRepository productFeedbackRepository;
    private final ProductFeedbackMapper productFeedbackMapper;

    @Override
    public Integer saveFeedback(ProductFeedback productFeedback) {
        return productFeedbackRepository.saveFeedback(productFeedback);
    }

    @Override
    public void updateFeedback(ProductFeedback productFeedback) {
        productFeedbackRepository.updateFeedback(productFeedback);
    }

    @Override
    public void deleteFeedback(Integer feedbackId) {
        productFeedbackRepository.deleteFeedback(feedbackId);
    }

    @Override
    public void deleteFeedbacksByProductId(Integer productId) {
        productFeedbackRepository.deleteFeedbacksByProductId(productId);
    }

    @Override
    public ProductFeedback getFeedbackById(Integer feedbackId) {
        return productFeedbackRepository.getFeedbackById(feedbackId).map(productFeedbackMapper);
    }

    @Override
    public List<ProductFeedback> getFeedbacksByProductId(Integer productId) {
        return productFeedbackRepository.getFeedbacksByProductId(productId).stream()
            .map(productFeedbackMapper::map)
            .toList();
    }

    @Override
    public boolean isFeedbackExist(Integer feedbackId) {
        return productFeedbackRepository.isFeedbackExist(feedbackId);
    }

    @Override
    public boolean canUserChangeFeedback(Integer feedbackId, Integer userId) {
        return productFeedbackRepository.canUserChangeFeedback(feedbackId, userId);
    }
}
