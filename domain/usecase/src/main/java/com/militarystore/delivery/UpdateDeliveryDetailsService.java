package com.militarystore.delivery;

import com.militarystore.entity.delivery.DeliveryDetails;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.in.delivery.UpdateDeliveryDetailsUseCase;
import com.militarystore.port.out.delivery.DeliveryDetailsPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateDeliveryDetailsService implements UpdateDeliveryDetailsUseCase {

    private final DeliveryDetailsPort deliveryDetailsPort;
    private final DeliveryDetailsValidator deliveryDetailsValidator;

    @Override
    public void updateDeliveryDetails(DeliveryDetails deliveryDetails) {
        checkIfDeliveryDetailsExists(deliveryDetails.id());
        deliveryDetailsValidator.validateDeliveryDetails(deliveryDetails);

        deliveryDetailsPort.updateDeliveryDetails(deliveryDetails);
        log.info("Delivery details with id {} was updated", deliveryDetails.id());
    }

    private void checkIfDeliveryDetailsExists(Integer id) {
        if (!deliveryDetailsPort.isDeliveryDetailsExists(id)) {
            throw new MsNotFoundException("Delivery details with id " + id + " not found");
        }
    }
}
