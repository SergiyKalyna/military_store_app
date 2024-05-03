package com.militarystore.delivery;

import com.militarystore.delivery.mapper.DeliveryDetailsMapper;
import com.militarystore.entity.delivery.DeliveryDetails;
import com.militarystore.port.out.delivery.DeliveryDetailsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryDetailsAdapter implements DeliveryDetailsPort {

    private final DeliveryDetailsRepository deliveryDetailsRepository;
    private final DeliveryDetailsMapper deliveryDetailsMapper;

    @Override
    public Integer addDeliveryDetails(DeliveryDetails deliveryDetails) {
        return deliveryDetailsRepository.addDeliveryDetails(deliveryDetails);
    }

    @Override
    public void updateDeliveryDetails(DeliveryDetails deliveryDetails) {
        deliveryDetailsRepository.updateDeliveryDetails(deliveryDetails);
    }

    @Override
    public void deleteDeliveryDetails(Integer id) {
        deliveryDetailsRepository.deleteDeliveryDetails(id);
    }

    @Override
    public DeliveryDetails getDeliveryDetails(Integer id) {
        var deliveryDetailsRecord = deliveryDetailsRepository.getDeliveryDetails(id);

        return deliveryDetailsMapper.map(deliveryDetailsRecord);
    }

    @Override
    public boolean isDeliveryDetailsExists(Integer id) {
        return deliveryDetailsRepository.isDeliveryDetailsExists(id);
    }
}
