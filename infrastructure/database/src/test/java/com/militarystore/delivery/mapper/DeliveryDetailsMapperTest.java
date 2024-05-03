package com.militarystore.delivery.mapper;

import com.militarystore.entity.delivery.DeliveryDetails;
import com.militarystore.jooq.tables.records.DeliveryDetailsRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryDetailsMapperTest {

    private DeliveryDetailsMapper deliveryDetailsMapper;

    @BeforeEach
    void setUp() {
        deliveryDetailsMapper = new DeliveryDetailsMapper();
    }

    @Test
    void map() {
        var deliveryDetailsRecord = new DeliveryDetailsRecord();
        deliveryDetailsRecord.setId(1);
        deliveryDetailsRecord.setCity("City");
        deliveryDetailsRecord.setPostNumber(11);
        deliveryDetailsRecord.setRecipientName("Recipient name");
        deliveryDetailsRecord.setRecipientPhone("Recipient phone");

        var expectedResult = DeliveryDetails.builder()
            .id(1)
            .city("City")
            .postNumber(11)
            .recipientName("Recipient name")
            .recipientPhone("Recipient phone")
            .build();

        assertThat(deliveryDetailsMapper.map(deliveryDetailsRecord)).isEqualTo(expectedResult);
    }
}