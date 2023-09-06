package com.example.order.domain.delivery;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DeliveryStatus {

    READY("준비"),
    COMP("배송중"),
    FINISH("배송 완료");

    private final String text;
}
