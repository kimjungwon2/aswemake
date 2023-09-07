package com.example.order.domain.delivery;

import com.example.order.common.domain.BaseTimeEntity;
import com.example.order.domain.order.Order;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    private Integer deliveryPrice;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Builder
    public Delivery(Long id,
            Address address,
            Integer deliveryPrice,
            DeliveryStatus status
    ) {
        this.id = id;
        this.order = order;
        this.address = address;
        this.deliveryPrice = deliveryPrice;
        this.status = status;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
