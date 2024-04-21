package com.militarystore.product.feedback;

import com.militarystore.entity.product.ProductFeedback;
import com.militarystore.entity.user.model.Role;
import com.militarystore.jooq.tables.records.ProductFeedbacksRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.militarystore.jooq.Tables.PRODUCT_FEEDBACKS;
import static com.militarystore.jooq.Tables.USERS;

@Repository
@RequiredArgsConstructor
public class ProductFeedbackRepository {

    private final DSLContext dslContext;

    public Integer saveFeedback(ProductFeedback productFeedback) {
        return dslContext.insertInto(PRODUCT_FEEDBACKS)
            .set(PRODUCT_FEEDBACKS.PRODUCT_ID, productFeedback.productId())
            .set(PRODUCT_FEEDBACKS.USER_ID, productFeedback.userId())
            .set(PRODUCT_FEEDBACKS.FEEDBACK, productFeedback.feedback())
            .set(PRODUCT_FEEDBACKS.DATE_TIME, productFeedback.dateTime())
            .returning(PRODUCT_FEEDBACKS.ID)
            .fetchOne(PRODUCT_FEEDBACKS.ID);
    }

    public void updateFeedback(ProductFeedback productFeedback) {
        dslContext.update(PRODUCT_FEEDBACKS)
            .set(PRODUCT_FEEDBACKS.FEEDBACK, productFeedback.feedback())
            .set(PRODUCT_FEEDBACKS.DATE_TIME, productFeedback.dateTime())
            .where(PRODUCT_FEEDBACKS.ID.eq(productFeedback.id()))
            .execute();
    }

    public List<ProductFeedbacksRecord> getFeedbacksByProductId(Integer productId) {
        return dslContext.selectFrom(PRODUCT_FEEDBACKS)
            .where(PRODUCT_FEEDBACKS.PRODUCT_ID.eq(productId))
            .orderBy(PRODUCT_FEEDBACKS.DATE_TIME.desc())
            .fetch();
    }

    public ProductFeedbacksRecord getFeedbackById(Integer feedbackId) {
        return dslContext.selectFrom(PRODUCT_FEEDBACKS)
            .where(PRODUCT_FEEDBACKS.ID.eq(feedbackId))
            .fetchOneInto(ProductFeedbacksRecord.class);
    }

    public void deleteFeedbacksByProductId(Integer productId) {
        dslContext.deleteFrom(PRODUCT_FEEDBACKS)
            .where(PRODUCT_FEEDBACKS.PRODUCT_ID.eq(productId))
            .execute();
    }

    public void deleteFeedback(Integer feedbackId) {
        dslContext.deleteFrom(PRODUCT_FEEDBACKS)
            .where(PRODUCT_FEEDBACKS.ID.eq(feedbackId))
            .execute();
    }

    public boolean isFeedbackExist(Integer feedbackId) {
        return dslContext.fetchExists(
            dslContext.selectFrom(PRODUCT_FEEDBACKS)
                .where(PRODUCT_FEEDBACKS.ID.eq(feedbackId))
        );
    }

    public boolean canUserChangeFeedback(Integer feedbackId, Integer userId) {
        return dslContext.fetchExists(
            dslContext.selectFrom(PRODUCT_FEEDBACKS).asTable()
                .innerJoin(USERS).on(USERS.ID.eq(PRODUCT_FEEDBACKS.USER_ID))
                .where(PRODUCT_FEEDBACKS.ID.eq(feedbackId)
                    .and(USERS.ID.eq(userId))
                    .or(USERS.ROLE.eq(Role.ADMIN.name()))
                    .or(USERS.ROLE.eq(Role.SUPER_ADMIN.name()))));
    }
}
