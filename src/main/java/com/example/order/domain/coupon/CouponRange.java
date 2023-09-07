package com.example.order.domain.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponRange {
    ORDER("주문 전체 (배달비 제외)"),
    PRODUCT("특정 상품 (모든 개수)");

    private final String text;
}