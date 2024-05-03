package com.militarystore.delivery;

import com.militarystore.entity.delivery.DeliveryDetails;
import com.militarystore.exception.MsValidationException;
import org.springframework.stereotype.Component;

import static com.militarystore.utils.PhoneNumberValidator.validatePhone;
import static java.util.Objects.isNull;

@Component
public class DeliveryDetailsValidator {

    public void validateDeliveryDetails(DeliveryDetails deliveryDetails) {
        if (isNull(deliveryDetails.city()) || deliveryDetails.city().isBlank()) {
            throw new MsValidationException("City should not be empty");
        }

        if (isNull(deliveryDetails.postNumber()) || deliveryDetails.postNumber() < 1) {
            throw new MsValidationException("Post number should not be empty or less then 1");
        }

        if (isNull(deliveryDetails.recipientName()) || deliveryDetails.recipientName().isBlank()) {
            throw new MsValidationException("Recipient name should not be empty");
        }

        validatePhone(deliveryDetails.recipientPhone());
    }
}
