package com.militarystore.product;

import com.militarystore.entity.product.ProductFeedback;
import com.militarystore.exception.MsAccessDeniedException;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.out.product.ProductFeedbackPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductFeedbackServiceTest {

    private static final int USER_ID = 1;
    private static final int PRODUCT_ID = 111;
    private static final int FEEDBACK_ID = 11;

    @Mock
    private ProductFeedbackPort productFeedbackPort;

    private ProductFeedbackService productFeedbackService;

    @BeforeEach
    void setUp() {
        productFeedbackService = new ProductFeedbackService(productFeedbackPort);
    }

    @Test
    void saveFeedback_shouldThrowException_whenFeedbackMessageIsNull() {
        var feedback = ProductFeedback.builder()
            .productId(PRODUCT_ID)
            .userId(USER_ID)
            .build();

        assertThrows(MsValidationException.class, () -> productFeedbackService.saveFeedback(feedback));
    }

    @Test
    void saveFeedback_shouldThrowException_whenFeedbackMessageIsEmpty() {
        var feedback = ProductFeedback.builder()
            .productId(PRODUCT_ID)
            .userId(USER_ID)
            .feedback("")
            .build();

        assertThrows(MsValidationException.class, () -> productFeedbackService.saveFeedback(feedback));
    }

    @Test
    void saveFeedback_shouldSave_whenFeedbackIsValid(){
        var feedback = ProductFeedback.builder()
            .productId(PRODUCT_ID)
            .userId(USER_ID)
            .feedback("Good product")
            .dateTime(LocalDateTime.now())
            .build();

        productFeedbackService.saveFeedback(feedback);

        verify(productFeedbackPort).saveFeedback(feedback);
    }

    @Test
    void updateFeedback_shouldThrowException_whenUserHaveNoAccessToChangeFeedback() {
        var feedback = ProductFeedback.builder()
            .id(FEEDBACK_ID)
            .productId(PRODUCT_ID)
            .userId(USER_ID)
            .feedback("Good product")
            .dateTime(LocalDateTime.now())
            .build();

        when(productFeedbackPort.canUserChangeFeedback(FEEDBACK_ID, USER_ID)).thenReturn(false);

        assertThrows(MsAccessDeniedException.class, () -> productFeedbackService.updateFeedback(feedback));
    }

    @Test
    void updateFeedback_shouldThrowException_whenFeedbackMessageIsNull() {
        var feedback = ProductFeedback.builder()
            .id(FEEDBACK_ID)
            .productId(PRODUCT_ID)
            .userId(USER_ID)
            .build();

        when(productFeedbackPort.canUserChangeFeedback(FEEDBACK_ID, USER_ID)).thenReturn(true);

        assertThrows(MsValidationException.class, () -> productFeedbackService.updateFeedback(feedback));
    }

    @Test
    void updateFeedback_shouldThrowException_whenFeedbackMessageIsEmpty() {
        var feedback = ProductFeedback.builder()
            .id(FEEDBACK_ID)
            .productId(PRODUCT_ID)
            .userId(USER_ID)
            .feedback("")
            .build();

        when(productFeedbackPort.canUserChangeFeedback(FEEDBACK_ID, USER_ID)).thenReturn(true);

        assertThrows(MsValidationException.class, () -> productFeedbackService.updateFeedback(feedback));
    }

    @Test
    void updateFeedback_shouldUpdate_whenFeedbackIsValid() {
        var feedback = ProductFeedback.builder()
            .id(FEEDBACK_ID)
            .productId(PRODUCT_ID)
            .userId(USER_ID)
            .feedback("Good product")
            .dateTime(LocalDateTime.now())
            .build();

        when(productFeedbackPort.canUserChangeFeedback(FEEDBACK_ID, USER_ID)).thenReturn(true);

        productFeedbackService.updateFeedback(feedback);

        verify(productFeedbackPort).updateFeedback(feedback);
    }

    @Test
    void deleteFeedback_shouldThrowException_whenUserHaveNoAccessToChangeFeedback() {
        when(productFeedbackPort.canUserChangeFeedback(FEEDBACK_ID, USER_ID)).thenReturn(false);

        assertThrows(MsAccessDeniedException.class, () -> productFeedbackService.deleteFeedback(FEEDBACK_ID, USER_ID));
    }

    @Test
    void deleteFeedback_shouldDelete_whenUserHaveAccessToChangeFeedback() {
        when(productFeedbackPort.canUserChangeFeedback(FEEDBACK_ID, USER_ID)).thenReturn(true);

        productFeedbackService.deleteFeedback(FEEDBACK_ID, USER_ID);

        verify(productFeedbackPort).deleteFeedback(FEEDBACK_ID);
    }

    @Test
    void getFeedbackById_shouldThrowException_whenFeedbackDoesNotExist() {
        when(productFeedbackPort.isFeedbackExist(FEEDBACK_ID)).thenReturn(false);

        assertThrows(MsNotFoundException.class, () -> productFeedbackService.getFeedbackById(FEEDBACK_ID));
    }

    @Test
    void getFeedbackById_shouldReturnFeedback() {
        var feedback = ProductFeedback.builder().build();

        when(productFeedbackPort.isFeedbackExist(FEEDBACK_ID)).thenReturn(true);
        when(productFeedbackPort.getFeedbackById(FEEDBACK_ID)).thenReturn(feedback);

        assertThat(productFeedbackService.getFeedbackById(FEEDBACK_ID)).isEqualTo(feedback);
    }


    @Test
    void getFeedbacksByProductId() {
        var feedbacks = List.of(ProductFeedback.builder().build());

        when(productFeedbackPort.getFeedbacksByProductId(PRODUCT_ID)).thenReturn(feedbacks);

        assertThat(productFeedbackService.getFeedbacksByProductId(PRODUCT_ID)).isEqualTo(feedbacks);
    }
}