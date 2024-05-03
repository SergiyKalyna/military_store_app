package com.militarystore.delivery.mapper;

import com.militarystore.entity.delivery.DeliveryDetails;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.militarystore.jooq.Tables.DELIVERY_DETAILS;

@Component
public class DeliveryDetailsMapper implements RecordMapper<Record, DeliveryDetails> {

    @Override
    public DeliveryDetails map(Record deliveryDetailsRecord) {
        return DeliveryDetails.builder()
            .id(deliveryDetailsRecord.get(DELIVERY_DETAILS.ID))
            .city(deliveryDetailsRecord.get(DELIVERY_DETAILS.CITY))
            .postNumber(deliveryDetailsRecord.get(DELIVERY_DETAILS.POST_NUMBER))
            .recipientName(deliveryDetailsRecord.get(DELIVERY_DETAILS.RECIPIENT_NAME))
            .recipientPhone(deliveryDetailsRecord.get(DELIVERY_DETAILS.RECIPIENT_PHONE))
            .build();
    }
}
