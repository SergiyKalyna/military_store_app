package com.militarystore.delivery;

import com.militarystore.converter.delivery.DeliveryDetailsConverter;
import com.militarystore.model.dto.delivery.DeliveryDetailsDto;
import com.militarystore.port.in.delivery.UpdateDeliveryDetailsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/delivery-details")
public class UpdateDeliveryDetailsController {

    private final UpdateDeliveryDetailsUseCase updateDeliveryDetailsUseCase;
    private final DeliveryDetailsConverter deliveryDetailsConverter;

    @PutMapping()
    public void updateDeliveryDetails(@RequestBody DeliveryDetailsDto deliveryDetailsDto) {
        var deliveryDetails = deliveryDetailsConverter.convertToDeliveryDetails(deliveryDetailsDto);

        updateDeliveryDetailsUseCase.updateDeliveryDetails(deliveryDetails);
    }
}
