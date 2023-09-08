package com.example.order.service.discount;

import com.example.order.domain.coupon.Coupon;
import com.example.order.domain.coupon.CouponType;
import org.springframework.stereotype.Component;

@Component
public class FixDiscountPolicy implements DiscountPolicy{

    @Override
    public int discount(Coupon coupon, int price) {
        if(coupon.getType()== CouponType.FIX) {
            checkPositiveData(coupon);

            return coupon.getDiscountData();
        } else{
            throw new IllegalStateException("쿠폰 적용이 잘못됐습니다.");
        }
    }

    private void checkPositiveData(Coupon coupon) {
        if(coupon.getDiscountData()<=0){
            throw new IllegalStateException("할인 금액은 양수여야 합니다.");
        }
    }
}
