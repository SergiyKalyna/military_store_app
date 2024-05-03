package com.militarystore.delivery;

import com.militarystore.delivery.mapper.DeliveryDetailsMapper;
import com.militarystore.entity.delivery.DeliveryDetails;
import com.militarystore.jooq.tables.records.DeliveryDetailsRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryDetailsAdapterTest {

    private static final Integer DELIVERY_DETAILS_ID = 1;

    @Mock
    private DeliveryDetailsRepository deliveryDetailsRepository;

    @Mock
    private DeliveryDetailsMapper deliveryDetailsMapper;

    private DeliveryDetailsAdapter deliveryDetailsAdapter;

    @BeforeEach
    void setUp() {
        deliveryDetailsAdapter = new DeliveryDetailsAdapter(deliveryDetailsRepository, deliveryDetailsMapper);
    }

    @Test
    void addDeliveryDetails() {
        var deliveryDetails = DeliveryDetails.builder().build();

        when(deliveryDetailsRepository.addDeliveryDetails(deliveryDetails)).thenReturn(DELIVERY_DETAILS_ID);

        assertThat(deliveryDetailsAdapter.addDeliveryDetails(deliveryDetails)).isEqualTo(DELIVERY_DETAILS_ID);
    }

    @Test
    void updateDeliveryDetails() {
        var deliveryDetails = DeliveryDetails.builder().build();

        deliveryDetailsAdapter.updateDeliveryDetails(deliveryDetails);

        verify(deliveryDetailsRepository).updateDeliveryDetails(deliveryDetails);
    }

    @Test
    void deleteDeliveryDetails() {
        deliveryDetailsAdapter.deleteDeliveryDetails(DELIVERY_DETAILS_ID);

        verify(deliveryDetailsRepository).deleteDeliveryDetails(DELIVERY_DETAILS_ID);
    }

    @Test
    void getDeliveryDetails() {
        var deliveryDetailsRecord = new DeliveryDetailsRecord();
        var deliveryDetails = DeliveryDetails.builder().build();

        when(deliveryDetailsRepository.getDeliveryDetails(DELIVERY_DETAILS_ID)).thenReturn(deliveryDetailsRecord);
        when(deliveryDetailsMapper.map(deliveryDetailsRecord)).thenReturn(deliveryDetails);

        assertThat(deliveryDetailsAdapter.getDeliveryDetails(DELIVERY_DETAILS_ID)).isEqualTo(deliveryDetails);
    }

    @Test
    void isDeliveryDetailsExists() {
        when(deliveryDetailsRepository.isDeliveryDetailsExists(DELIVERY_DETAILS_ID)).thenReturn(true);

        assertTrue(deliveryDetailsAdapter.isDeliveryDetailsExists(DELIVERY_DETAILS_ID));
    }
}