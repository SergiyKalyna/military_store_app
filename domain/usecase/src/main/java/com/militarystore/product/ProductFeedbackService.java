package com.militarystore.product;

import com.militarystore.entity.product.ProductFeedback;
import com.militarystore.exception.MsAccessDeniedException;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.in.product.ProductFeedbackUseCase;
import com.militarystore.port.out.product.ProductFeedbackPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductFeedbackService implements ProductFeedbackUseCase {

    private final ProductFeedbackPort productFeedbackPort;

    @Override
    public Integer saveFeedback(ProductFeedback productFeedback) {
        validateFeedback(productFeedback.feedback());

        var feedbackId = productFeedbackPort.saveFeedback(productFeedback);
        log.info("New feedback was added with id - {} for product with id - {}", feedbackId, productFeedback.productId());

        return feedbackId;
    }

    @Override
    public void updateFeedback(ProductFeedback productFeedback) {
        checkIfUserCanChangeFeedback(productFeedback.id(), productFeedback.userId());
        validateFeedback(productFeedback.feedback());

        productFeedbackPort.updateFeedback(productFeedback);

        log.info("Feedback with id '{}' was updated", productFeedback.id());
    }

    @Override
    public void deleteFeedback(Integer feedbackId, Integer userId) {
        checkIfUserCanChangeFeedback(feedbackId, userId);

        productFeedbackPort.deleteFeedback(feedbackId);

        log.info("Feedback with id '{}' was deleted", feedbackId);
    }

    @Override
    public ProductFeedback getFeedbackById(Integer feedbackId) {
        checkFeedbackExisting(feedbackId);

        return productFeedbackPort.getFeedbackById(feedbackId);
    }

    @Override
    public List<ProductFeedback> getFeedbacksByProductId(Integer productId) {
        return productFeedbackPort.getFeedbacksByProductId(productId);
    }

    private void validateFeedback(String feedback) {
        if (isNull(feedback) || feedback.isBlank()) {
            throw new MsValidationException("Feedback should not be empty");
        }
    }

    private void checkFeedbackExisting(Integer feedbackId) {
        if (!productFeedbackPort.isFeedbackExist(feedbackId)) {
            throw new MsNotFoundException("Feedback with id " + feedbackId + " does not exist");
        }
    }

    private void checkIfUserCanChangeFeedback(Integer feedbackId, Integer userId) {
        if (!productFeedbackPort.canUserChangeFeedback(feedbackId, userId)) {
            throw new MsAccessDeniedException("User with id " + userId + " haven't rights to change feedback with id " + feedbackId);
        }
    }
}
