package com.militarystore.delivery;

import com.militarystore.entity.delivery.DeliveryDetails;
import com.militarystore.jooq.tables.records.DeliveryDetailsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.militarystore.jooq.tables.DeliveryDetails.DELIVERY_DETAILS;

@Repository
@RequiredArgsConstructor
public class DeliveryDetailsRepository {

    private final DSLContext dslContext;

    public Integer addDeliveryDetails(DeliveryDetails deliveryDetails) {
        return dslContext.insertInto(DELIVERY_DETAILS)
            .set(DELIVERY_DETAILS.CITY, deliveryDetails.city())
            .set(DELIVERY_DETAILS.POST_NUMBER, deliveryDetails.postNumber())
            .set(DELIVERY_DETAILS.RECIPIENT_NAME, deliveryDetails.recipientName())
            .set(DELIVERY_DETAILS.RECIPIENT_PHONE, deliveryDetails.recipientPhone())
            .returning(DELIVERY_DETAILS.ID)
            .fetchOne(DELIVERY_DETAILS.ID);
    }

    public void updateDeliveryDetails(DeliveryDetails deliveryDetails) {
        dslContext.update(DELIVERY_DETAILS)
            .set(DELIVERY_DETAILS.CITY, deliveryDetails.city())
            .set(DELIVERY_DETAILS.POST_NUMBER, deliveryDetails.postNumber())
            .set(DELIVERY_DETAILS.RECIPIENT_NAME, deliveryDetails.recipientName())
            .set(DELIVERY_DETAILS.RECIPIENT_PHONE, deliveryDetails.recipientPhone())
            .where(DELIVERY_DETAILS.ID.eq(deliveryDetails.id()))
            .execute();
    }

    public void deleteDeliveryDetails(Integer id) {
        dslContext.deleteFrom(DELIVERY_DETAILS)
            .where(DELIVERY_DETAILS.ID.eq(id))
            .execute();
    }

    public DeliveryDetailsRecord getDeliveryDetails(Integer id) {
        return dslContext.selectFrom(DELIVERY_DETAILS)
            .where(DELIVERY_DETAILS.ID.eq(id))
            .fetchOneInto(DeliveryDetailsRecord.class);
    }

    public boolean isDeliveryDetailsExists(Integer id) {
        return dslContext.fetchExists(
            dslContext.selectFrom(DELIVERY_DETAILS)
                .where(DELIVERY_DETAILS.ID.eq(id))
        );
    }
}
