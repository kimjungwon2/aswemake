package com.example.order.service.discount;

import com.example.order.domain.coupon.Coupon;
import com.example.order.domain.coupon.CouponType;
import org.springframework.stereotype.Component;

@Component
public class RateDiscountPolicy implements DiscountPolicy{

    @Override
    public int discount(Coupon coupon, int price) {
        if(coupon.getType()== CouponType.RATE) {
            validateRatio(coupon);

            return price * coupon.getDiscountData() / 100;
        } else{
            throw new IllegalStateException("쿠폰 적용이 잘못됐습니다.");
        }
    }

    private void validateRatio(Coupon coupon) {
        if(coupon.getDiscountData()>=101 && coupon.getDiscountData()<=0){
            throw new IllegalStateException("적절한 할인 비율이 아닙니다.");
        }
    }
}
