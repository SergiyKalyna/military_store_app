package com.militarystore.delivery;

import com.militarystore.entity.delivery.DeliveryDetails;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.out.delivery.DeliveryDetailsPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateDeliveryDetailsServiceTest {

    @Mock
    private DeliveryDetailsPort deliveryDetailsPort;

    private UpdateDeliveryDetailsService updateDeliveryDetailsService;

    @BeforeEach
    void setUp() {
        updateDeliveryDetailsService = new UpdateDeliveryDetailsService(deliveryDetailsPort, new DeliveryDetailsValidator());
    }

    @Test
    void updateDeliveryDetails_whenDeliveryDetailsDoesNotExist_shouldThrowException() {
        var deliveryDetails = DeliveryDetails.builder().id(1).build();

        when(deliveryDetailsPort.isDeliveryDetailsExists(1)).thenReturn(false);

        assertThrows(MsNotFoundException.class, () -> updateDeliveryDetailsService.updateDeliveryDetails(deliveryDetails));
    }

    @Test
    void updateDeliveryDetails_whenDeliveryDetailsCityIsNull_shouldThrowException() {
        var deliveryDetails = DeliveryDetails.builder().id(1).build();

        when(deliveryDetailsPort.isDeliveryDetailsExists(1)).thenReturn(true);

        assertThrows(MsValidationException.class, () -> updateDeliveryDetailsService.updateDeliveryDetails(deliveryDetails));
    }

    @Test
    void updateDeliveryDetails_whenDeliveryDetailsCityIsBlank_shouldThrowException() {
        var deliveryDetails = DeliveryDetails.builder().id(1).city("").build();

        when(deliveryDetailsPort.isDeliveryDetailsExists(1)).thenReturn(true);

        assertThrows(MsValidationException.class, () -> updateDeliveryDetailsService.updateDeliveryDetails(deliveryDetails));
    }

    @Test
    void updateDeliveryDetails_whenDeliveryDetailsCityIncludeOnlySpaces_shouldThrowException() {
        var deliveryDetails = DeliveryDetails.builder().id(1).city("     ").build();

        when(deliveryDetailsPort.isDeliveryDetailsExists(1)).thenReturn(true);

        assertThrows(MsValidationException.class, () -> updateDeliveryDetailsService.updateDeliveryDetails(deliveryDetails));
    }

    @Test
    void updateDeliveryDetails_whenDeliveryDetailsPostNumberIsNull_shouldThrowException() {
        var deliveryDetails = DeliveryDetails.builder().id(1).city("City").build();

        when(deliveryDetailsPort.isDeliveryDetailsExists(1)).thenReturn(true);

        assertThrows(MsValidationException.class, () -> updateDeliveryDetailsService.updateDeliveryDetails(deliveryDetails));
    }

    @Test
    void updateDeliveryDetails_whenDeliveryDetailsPostNumberIsNegative_shouldThrowException() {
        var deliveryDetails = DeliveryDetails.builder().id(1).city("City").postNumber(-1).build();

        when(deliveryDetailsPort.isDeliveryDetailsExists(1)).thenReturn(true);

        assertThrows(MsValidationException.class, () -> updateDeliveryDetailsService.updateDeliveryDetails(deliveryDetails));
    }

    @Test
    void updateDeliveryDetails_whenDeliveryDetailsRecipientNameIsNull_shouldThrowException() {
        var deliveryDetails = DeliveryDetails.builder().id(1).city("City").postNumber(1).build();

        when(deliveryDetailsPort.isDeliveryDetailsExists(1)).thenReturn(true);

        assertThrows(MsValidationException.class, () -> updateDeliveryDetailsService.updateDeliveryDetails(deliveryDetails));
    }

    @Test
    void updateDeliveryDetails_whenDeliveryDetailsRecipientNameIsBlank_shouldThrowException() {
        var deliveryDetails = DeliveryDetails.builder().id(1).city("City").postNumber(1).recipientName("").build();

        when(deliveryDetailsPort.isDeliveryDetailsExists(1)).thenReturn(true);

        assertThrows(MsValidationException.class, () -> updateDeliveryDetailsService.updateDeliveryDetails(deliveryDetails));
    }

    @Test
    void updateDeliveryDetails_whenDeliveryDetailsRecipientNameIncludeOnlySpaces_shouldThrowException() {
        var deliveryDetails = DeliveryDetails.builder().id(1).city("City").postNumber(1).recipientName("     ").build();

        when(deliveryDetailsPort.isDeliveryDetailsExists(1)).thenReturn(true);

        assertThrows(MsValidationException.class, () -> updateDeliveryDetailsService.updateDeliveryDetails(deliveryDetails));
    }

    @Test
    void updateDeliveryDetails_whenDeliveryDetailsRecipientPhoneIsInvalid_shouldThrowException() {
        var deliveryDetails = DeliveryDetails.builder().id(1).city("City").postNumber(1).recipientName("Recipient name").recipientPhone("123").build();

        when(deliveryDetailsPort.isDeliveryDetailsExists(1)).thenReturn(true);

        assertThrows(MsValidationException.class, () -> updateDeliveryDetailsService.updateDeliveryDetails(deliveryDetails));
    }

    @Test
    void updateDeliveryDetails() {
        var deliveryDetails = DeliveryDetails.builder()
            .id(1).city("City")
            .postNumber(1)
            .recipientName("Recipient name")
            .recipientPhone("+12345678932")
            .build();

        when(deliveryDetailsPort.isDeliveryDetailsExists(1)).thenReturn(true);

        updateDeliveryDetailsService.updateDeliveryDetails(deliveryDetails);

        verify(deliveryDetailsPort).updateDeliveryDetails(deliveryDetails);
    }
}