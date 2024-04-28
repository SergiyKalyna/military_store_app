package com.militarystore.product.feedback;

import com.militarystore.entity.product.ProductFeedback;
import com.militarystore.jooq.tables.records.ProductFeedbacksRecord;
import com.militarystore.product.mapper.ProductFeedbackMapper;
import org.jooq.Record5;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductFeedbackAdapterTest {

    @Mock
    private ProductFeedbackRepository productFeedbackRepository;

    @Mock
    private ProductFeedbackMapper productFeedbackMapper;

    private ProductFeedbackAdapter productFeedbackAdapter;

    @BeforeEach
    void setUp() {
        productFeedbackAdapter = new ProductFeedbackAdapter(productFeedbackRepository, productFeedbackMapper);
    }

    @Test
    void saveFeedback() {
        var feedback = ProductFeedback.builder().build();

        when(productFeedbackRepository.saveFeedback(feedback)).thenReturn(1);

        assertThat(productFeedbackAdapter.saveFeedback(feedback)).isEqualTo(1);
    }

    @Test
    void updateFeedback() {
        var feedback = ProductFeedback.builder().build();

        productFeedbackAdapter.updateFeedback(feedback);

        verify(productFeedbackRepository).updateFeedback(feedback);
    }

    @Test
    void deleteFeedback() {
        var feedbackId = 1;

        productFeedbackAdapter.deleteFeedback(feedbackId);

        verify(productFeedbackRepository).deleteFeedback(feedbackId);
    }

    @Test
    void deleteFeedbacksByProductId() {
        var productId = 1;

        productFeedbackAdapter.deleteFeedbacksByProductId(productId);

        verify(productFeedbackRepository).deleteFeedbacksByProductId(productId);
    }

    @Test
    void getFeedbackById() {
        var feedbackId = 1;
        var feedbackRecord = new ProductFeedbacksRecord();
        var feedback = ProductFeedback.builder().build();

        when(productFeedbackRepository.getFeedbackById(feedbackId)).thenReturn(feedbackRecord);
        when(productFeedbackMapper.map(feedbackRecord)).thenReturn(feedback);

        assertThat(productFeedbackAdapter.getFeedbackById(feedbackId)).isEqualTo(feedback);
    }

    @Test
    void getFeedbacksByProductId() {
        var productId = 1;
        var feedbackRecord = mock(Record5.class);
        var feedback = ProductFeedback.builder().build();

        when(productFeedbackRepository.getFeedbacksByProductId(productId)).thenReturn(List.of(feedbackRecord));
        when(productFeedbackMapper.map(feedbackRecord)).thenReturn(feedback);

        assertThat(productFeedbackAdapter.getFeedbacksByProductId(productId)).containsExactly(feedback);
    }

    @Test
    void isFeedbackExist() {
        var feedbackId = 1;

        when(productFeedbackRepository.isFeedbackExist(feedbackId)).thenReturn(true);

        assertTrue(productFeedbackAdapter.isFeedbackExist(feedbackId));
    }

    @Test
    void canUserChangeFeedback() {
        var feedbackId = 1;
        var userId = 1;

        when(productFeedbackRepository.canUserChangeFeedback(feedbackId, userId)).thenReturn(true);

        assertTrue(productFeedbackAdapter.canUserChangeFeedback(feedbackId, userId));
    }

    @Test
    void deleteFeedbacksByUserId() {
        var userId = 1;

        productFeedbackAdapter.deleteFeedbacksByUserId(userId);

        verify(productFeedbackRepository).deleteFeedbacksByUserId(userId);
    }
}