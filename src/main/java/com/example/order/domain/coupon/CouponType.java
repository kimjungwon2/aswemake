package com.example.order.domain.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponType {
    FIX("고정"),
    RATE("비율");

    private final String text;
}
