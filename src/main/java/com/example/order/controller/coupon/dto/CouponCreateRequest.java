package com.example.order.controller.coupon.dto;

import com.example.order.domain.coupon.Coupon;
import com.example.order.domain.coupon.CouponRange;
import com.example.order.domain.coupon.CouponType;
import com.example.order.domain.order.Order;
import com.example.order.domain.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponCreateRequest {

    CouponType couponType;

    CouponRange couponRange;

    Integer discountData;

    @Builder
    public CouponCreateRequest(
            CouponType couponType,
            CouponRange couponRange,
            Integer discountData
    ) {
        this.couponType = couponType;
        this.couponRange = couponRange;
        this.discountData = discountData;
    }

}
