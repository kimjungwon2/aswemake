package com.example.order.service.coupon;

import com.example.order.controller.coupon.dto.CouponCreateRequest;
import com.example.order.domain.coupon.Coupon;
import com.example.order.domain.coupon.CouponRange;
import com.example.order.domain.product.Product;
import com.example.order.repository.CouponRepository;
import com.example.order.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final ProductRepository productRepository;

    public Long createCoupon(CouponCreateRequest request) {

        if(isProductRange(request)){
            Product product = productRepository.findByName(request.getProductName())
                    .orElseThrow();

            Coupon coupon = request.toEntity(product);
            couponRepository.save(coupon);

            return coupon.getId();
        } else {
            Coupon coupon = request.toEntity();
            couponRepository.save(coupon);

            return coupon.getId();
        }
    }

    private boolean isProductRange(CouponCreateRequest request) {
        return request.getCouponRange() == CouponRange.PRODUCT;
    }
}
