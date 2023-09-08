package com.example.order.service.discount;

import com.example.order.domain.coupon.Coupon;
import com.example.order.domain.coupon.CouponType;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final Map<String, DiscountPolicy> policyMap;
    private final List<DiscountPolicy> policies;

    public int discount(Coupon coupon, int price){

        String discountCode;

        if(coupon.getType() == CouponType.FIX){
            discountCode = "fixDiscountPolicy";
        } else{
            discountCode = "rateDiscountPolicy";
        }

        DiscountPolicy discountPolicy = policyMap.get(discountCode);

        return discountPolicy.discount(coupon,price);
    }
}
