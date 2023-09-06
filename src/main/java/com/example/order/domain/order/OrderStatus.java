package com.example.order.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
    ORDER("주문"),
    CANCEL("취소");

    private final String text;
}
