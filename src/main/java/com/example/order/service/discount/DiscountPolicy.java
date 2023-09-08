package com.example.order.service.discount;

import com.example.order.domain.coupon.Coupon;

public interface DiscountPolicy {
    int discount(Coupon coupon, int price);
}
