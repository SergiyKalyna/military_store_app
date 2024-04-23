package com.militarystore.product;

import com.militarystore.converter.product.ProductFeedbackConverter;
import com.militarystore.model.dto.product.ProductFeedbackDto;
import com.militarystore.model.request.product.ProductFeedbackRequest;
import com.militarystore.port.in.product.ProductFeedbackUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("products/feedback")
@RequiredArgsConstructor
public class ProductFeedbackController {

    private final ProductFeedbackUseCase productFeedbackUseCase;
    private final ProductFeedbackConverter productFeedbackConverter;

    //@TODO: User id should be replaced with user context after adding security
    @PostMapping()
    public Integer saveFeedback(
        @RequestParam("userId") Integer userId,
        @RequestBody ProductFeedbackRequest productFeedbackRequest
    ) {
        var productFeedback = productFeedbackConverter.convertToProductFeedback(productFeedbackRequest, userId);

        return productFeedbackUseCase.saveFeedback(productFeedback);
    }

    //@TODO: User id should be replaced with user context after adding security
    @PutMapping("/{feedbackId}")
    public void updateFeedback(
        @PathVariable("feedbackId") Integer feedbackId,
        @RequestParam("userId") Integer userId,
        @RequestBody ProductFeedbackRequest productFeedbackRequest
    ) {
        var productFeedback = productFeedbackConverter.convertToProductFeedback(productFeedbackRequest, feedbackId, userId);

        productFeedbackUseCase.updateFeedback(productFeedback);
    }

    //@TODO: User id should be replaced with user context after adding security
    @DeleteMapping("/{feedbackId}")
    public void deleteFeedback(
        @PathVariable("feedbackId") Integer feedbackId,
        @RequestParam("userId") Integer userId
    ) {
        productFeedbackUseCase.deleteFeedback(feedbackId, userId);
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
