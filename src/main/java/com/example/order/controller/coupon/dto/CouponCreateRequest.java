package com.example.order.controller.coupon.dto;

import com.example.order.domain.coupon.Coupon;
import com.example.order.domain.coupon.CouponRange;
import com.example.order.domain.coupon.CouponType;
import com.example.order.domain.order.Order;
import com.example.order.domain.product.Product;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponCreateRequest {

    @NotNull(message="쿠폰 타입을 입력해주세요.")
    private CouponType couponType;

    @NotNull(message="적용할 쿠폰 범위를 입력해주세요.")
    private CouponRange couponRange;

    @NotNull(message="할인 비율 혹은 값을 입력해주세요.")
    private Integer discountData;

    private String productName;

    @Builder
    public CouponCreateRequest(
            CouponType couponType,
            CouponRange couponRange,
            Integer discountData,
            String productName
    ) {
        this.couponType = couponType;
        this.couponRange = couponRange;
        this.discountData = discountData;
        this.productName = productName;
    }

    public Coupon toEntity(Product product){
        if(couponRange == CouponRange.PRODUCT){
            return Coupon.builder()
                    .type(couponType)
                    .range(couponRange)
                    .discountData(discountData)
                    .product(product)
                    .build();
        } else{
            throw new IllegalStateException("쿠폰이 PRODUCT 타입이 아닙니다.");
        }
    }

    public Coupon toEntity(){
        if(couponRange == CouponRange.ORDER){
            return Coupon.builder()
                    .type(couponType)
                    .range(couponRange)
                    .discountData(discountData)
                    .build();
        } else {
            throw new IllegalStateException("쿠폰이 ORDER 타입이 아닙니다.");
        }
    }
}
