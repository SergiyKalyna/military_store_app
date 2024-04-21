package com.militarystore.product.mapper;

import com.militarystore.entity.product.ProductFeedback;
import org.jooq.RecordMapper;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.militarystore.jooq.Tables.PRODUCT_FEEDBACKS;

@Component
public class ProductFeedbackMapper implements RecordMapper<Record, ProductFeedback> {

    @Override
    public ProductFeedback map(Record feedbackRecord) {
        return ProductFeedback.builder()
            .id(feedbackRecord.get(PRODUCT_FEEDBACKS.ID))
            .productId(feedbackRecord.get(PRODUCT_FEEDBACKS.PRODUCT_ID))
            .userId(feedbackRecord.get(PRODUCT_FEEDBACKS.USER_ID))
            .feedback(feedbackRecord.get(PRODUCT_FEEDBACKS.FEEDBACK))
            .dateTime(feedbackRecord.get(PRODUCT_FEEDBACKS.DATE_TIME))
            .build();
    }
}
