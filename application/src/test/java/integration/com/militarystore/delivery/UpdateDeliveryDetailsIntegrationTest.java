package com.militarystore.delivery;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.delivery.DeliveryDetails;
import com.militarystore.jooq.tables.records.DeliveryDetailsRecord;
import com.militarystore.port.in.delivery.UpdateDeliveryDetailsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.militarystore.jooq.Tables.DELIVERY_DETAILS;
import static org.assertj.core.api.Assertions.assertThat;

class UpdateDeliveryDetailsIntegrationTest extends IntegrationTest {

    private static final int DELIVERY_DETAILS_ID = 1;

    @Autowired
    private UpdateDeliveryDetailsUseCase updateDeliveryDetailsUseCase;

    @Test
    void updateDeliveryDetails() {
        initializeDeliveryDetails();

        var deliveryDetails = DeliveryDetails.builder()
                .id(DELIVERY_DETAILS_ID)
                .city("Horishni Plavni")
                .postNumber(1)
                .recipientName("Harry Potter")
                .recipientPhone("+380637131277")
                .build();

        updateDeliveryDetailsUseCase.updateDeliveryDetails(deliveryDetails);

        var deliveryDetailsRecord = getDeliveryDetailsRecord();

        assertThat(deliveryDetailsRecord.getCity()).isEqualTo(deliveryDetails.city());
        assertThat(deliveryDetailsRecord.getPostNumber()).isEqualTo(deliveryDetails.postNumber());
        assertThat(deliveryDetailsRecord.getRecipientName()).isEqualTo(deliveryDetails.recipientName());
        assertThat(deliveryDetailsRecord.getRecipientPhone()).isEqualTo(deliveryDetails.recipientPhone());
    }

    private void initializeDeliveryDetails() {
        dslContext.insertInto(DELIVERY_DETAILS)
            .set(DELIVERY_DETAILS.ID, DELIVERY_DETAILS_ID)
            .set(DELIVERY_DETAILS.CITY, "Plavni")
            .set(DELIVERY_DETAILS.POST_NUMBER, 2)
            .set(DELIVERY_DETAILS.RECIPIENT_NAME, "Gregoriy Potter")
            .set(DELIVERY_DETAILS.RECIPIENT_PHONE, "+12345678932")
            .execute();
    }

    private DeliveryDetailsRecord getDeliveryDetailsRecord() {
        return dslContext.selectFrom(DELIVERY_DETAILS)
                .where(DELIVERY_DETAILS.ID.eq(DELIVERY_DETAILS_ID))
                .fetchOne();
    }
}
