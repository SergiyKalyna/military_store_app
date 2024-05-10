package com.militarystore.product;

import com.militarystore.converter.product.ProductFeedbackConverter;
import com.militarystore.entity.user.User;
import com.militarystore.model.dto.product.ProductFeedbackDto;
import com.militarystore.model.request.product.ProductFeedbackRequest;
import com.militarystore.port.in.product.ProductFeedbackUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("products/feedback")
@RequiredArgsConstructor
public class ProductFeedbackController {

    private final ProductFeedbackUseCase productFeedbackUseCase;
    private final ProductFeedbackConverter productFeedbackConverter;

    @PostMapping()
    public Integer saveFeedback(
        @RequestBody ProductFeedbackRequest productFeedbackRequest,
        @AuthenticationPrincipal User user
        ) {
        var productFeedback = productFeedbackConverter.convertToProductFeedback(productFeedbackRequest, user.id());

        return productFeedbackUseCase.saveFeedback(productFeedback);
    }

    @PutMapping("/{feedbackId}")
    public void updateFeedback(
        @PathVariable("feedbackId") Integer feedbackId,
        @RequestBody ProductFeedbackRequest productFeedbackRequest,
        @AuthenticationPrincipal User user
        ) {
        var productFeedback = productFeedbackConverter.convertToProductFeedback(productFeedbackRequest, feedbackId, user.id());

        productFeedbackUseCase.updateFeedback(productFeedback);
    }

    @DeleteMapping("/{feedbackId}")
    public void deleteFeedback(
        @PathVariable("feedbackId") Integer feedbackId,
        @AuthenticationPrincipal User user
    ) {
        productFeedbackUseCase.deleteFeedback(feedbackId, user.id());
    }

    @GetMapping("/{feedbackId}")
    public ProductFeedbackDto getFeedbackById(@PathVariable("feedbackId") Integer feedbackId) {
        var productFeedback = productFeedbackUseCase.getFeedbackById(feedbackId);

        return productFeedbackConverter.convertToProductFeedbackDto(productFeedback);
    }

    @GetMapping("/product/{productId}")
    public List<ProductFeedbackDto> getFeedbacksByProductId(@PathVariable("productId") Integer productId) {
        var productFeedbacks = productFeedbackUseCase.getFeedbacksByProductId(productId);

        return productFeedbacks.stream()
            .map(productFeedbackConverter::convertToProductFeedbackDto)
            .toList();
    }
}
