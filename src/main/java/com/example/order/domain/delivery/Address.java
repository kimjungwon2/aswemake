package com.example.order.domain.delivery;

import javax.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
    private String city;
    private String detailedAddress;
    private String zipcode;

    @Builder
    public Address(
            String city,
            String detailedAddress,
            String zipcode
    ) {
        this.city = city;
        this.detailedAddress = detailedAddress;
        this.zipcode = zipcode;
    }

    protected Address() {
    }
}
