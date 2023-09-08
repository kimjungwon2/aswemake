package com.example.order.controller.coupon;

import com.example.order.controller.coupon.dto.CouponCreateRequest;
import com.example.order.service.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/coupon/create")
    public Long createOrder(@Validated @RequestBody CouponCreateRequest request){
        return couponService.createCoupon(request);
    }
}
