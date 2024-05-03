package com.militarystore.port.out.delivery;

import com.militarystore.entity.delivery.DeliveryDetails;

public interface DeliveryDetailsPort {

    Integer addDeliveryDetails(DeliveryDetails deliveryDetails);

    void updateDeliveryDetails(DeliveryDetails deliveryDetails);

    void deleteDeliveryDetails(Integer id);

    DeliveryDetails getDeliveryDetails(Integer id);

    boolean isDeliveryDetailsExists(Integer id);
}
