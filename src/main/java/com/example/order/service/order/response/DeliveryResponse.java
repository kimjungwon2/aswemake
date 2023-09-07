package com.example.order.service.order.response;

import com.example.order.domain.delivery.Address;
import com.example.order.domain.delivery.Delivery;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeliveryResponse {

    private Long id;
    private Integer deliveryPrice;
    private Address address;

    @Builder
    public DeliveryResponse(Long id, Integer deliveryPrice, Address address) {
        this.id = id;
        this.deliveryPrice = deliveryPrice;
        this.address = address;
    }

    public static DeliveryResponse of(Delivery delivery) {
        return DeliveryResponse.builder()
                .id(delivery.getId())
                .deliveryPrice(delivery.getDeliveryPrice())
                .address(delivery.getAddress())
                .build();
    }
}
