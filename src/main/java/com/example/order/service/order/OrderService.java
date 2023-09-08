package com.example.order.service.order;

import com.example.order.controller.order.dto.OrderCreateRequest;
import com.example.order.domain.OrderProduct;
import com.example.order.domain.coupon.Coupon;
import com.example.order.domain.coupon.CouponRange;
import com.example.order.domain.coupon.CouponType;
import com.example.order.domain.delivery.Address;
import com.example.order.domain.delivery.Delivery;
import com.example.order.domain.delivery.DeliveryStatus;
import com.example.order.domain.member.Member;
import com.example.order.domain.order.Order;
import com.example.order.domain.product.Product;
import com.example.order.repository.DeliveryRepository;
import com.example.order.repository.MemberRepository;
import com.example.order.repository.OrderProductRepository;
import com.example.order.repository.OrderRepository;
import com.example.order.repository.ProductRepository;
import com.example.order.service.discount.DiscountService;
import java.util.HashMap;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    private final DiscountService discountService;

    private static final Integer DELIVERY_PRICE = 2500;

    @Transactional
    public Integer createOrder(OrderCreateRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new IllegalStateException("해당 이메일의 멤버가 존재하지 않습니다."));

        Delivery delivery = saveDelivery(request);

        Order order = saveOrder(request, member, delivery);

        int totalProductPrice = getTotalOrderProductPrice(request, order);

        if(request.getIsCoupon()==false || request.getCouponRange() == CouponRange.PRODUCT) {
            order.setTotalPrice(totalProductPrice);

            return totalProductPrice + delivery.getDeliveryPrice();
        }

        return applyOrderCoupon(request, delivery, order, totalProductPrice);
    }

    private int applyOrderCoupon(
            OrderCreateRequest request,
            Delivery delivery,
            Order order,
            int totalProductPrice
    ) {
        checkOrderCouponData(request);

        Coupon coupon = Coupon.builder()
                .range(request.getCouponRange())
                .type(request.getCouponType())
                .discountData(request.getDiscountData())
                .build();

        int totalPrice = totalProductPrice - discountService.discount(coupon, totalProductPrice);

        totalPrice = validateTotalPrice(totalPrice);

        order.setTotalPrice(totalPrice);

        return totalPrice + delivery.getDeliveryPrice();
    }

    private int validateTotalPrice(int totalPrice) {
        if(totalPrice <0){
            totalPrice = 0;
        }
        return totalPrice;
    }

    private void checkOrderCouponData(OrderCreateRequest request) {
        if(request.getDiscountData() == null || request.getCouponRange() == null || request.getCouponType() == null){
            throw new IllegalStateException("쿠폰의 데이터가 존재하지 않습니다.");
        }
    }

    private Order saveOrder(
            OrderCreateRequest request,
            Member member,
            Delivery delivery
    ) {
        Order order = request.toEntity();
        Order.createOrder(order, member, delivery);

        orderRepository.save(order);
        return order;
    }


    private int getTotalOrderProductPrice(OrderCreateRequest request, Order order) {
        int totalProductPrice = 0;

        HashMap<String, Integer> products = request.getProducts();
        Set<String> productNames = products.keySet();

        for(String productName: productNames){
            Product product = productRepository.findByName(productName)
                       .orElseThrow(() -> new IllegalStateException("해당 상품이 존재하지 않습니다."));

            int count = products.get(productName);

            isCountPositive(count);

            int orderPrice = count * product.getPrice();

            if(request.getIsCoupon() == true && request.getCouponRange() == CouponRange.PRODUCT){

                checkProductCouponData(request);


                orderPrice = productDiscountCoupon(request, product, orderPrice, count);


                saveOrderProduct(product, count, orderPrice, order);
                totalProductPrice += orderPrice;
            } else{
                saveOrderProduct(product, count, orderPrice, order);
                totalProductPrice += orderPrice;
            }
        }

        return totalProductPrice;
    }

    private int productDiscountCoupon(
            OrderCreateRequest request,
            Product product,
            int orderPrice,
            int count
    ) {
        if(product.getName().equals(request.getCouponProductName())){
            Product foundedProduct = productRepository.findByName(
                    request.getCouponProductName())
                    .orElseThrow(()-> new IllegalStateException("해당 쿠폰 이름이 존재하지 않습니다."));

            Coupon coupon = Coupon.builder()
                    .range(request.getCouponRange())
                    .type(request.getCouponType())
                    .discountData(request.getDiscountData())
                    .product(foundedProduct)
                    .build();

            if(coupon.getType() == CouponType.FIX){
                orderPrice = validateOrderPriceByFix(orderPrice, coupon, count);
            } else{
                orderPrice = validateOrderPriceByRate(orderPrice,coupon,count,product.getPrice());
            }
        }
        return orderPrice;
    }

    private int validateOrderPriceByFix(int orderPrice, Coupon coupon, int count) {
        if(orderPrice - discountService.discount(coupon, orderPrice) <=0){
            orderPrice = 0;
        } else{
            orderPrice -= count * discountService.discount(coupon, orderPrice);
        }
        return orderPrice;
    }

    private int validateOrderPriceByRate(int orderPrice, Coupon coupon, int count, int productPrice) {
        if(orderPrice - discountService.discount(coupon, productPrice) <=0){
            orderPrice = 0;
        } else{
            orderPrice -= count * discountService.discount(coupon, productPrice);
        }
        return orderPrice;
    }


    private void checkProductCouponData(OrderCreateRequest request) {
        if(request.getDiscountData() == null || request.getCouponRange() == null || request.getCouponType() == null || request.getCouponProductName() == null){
            throw new IllegalStateException("쿠폰의 데이터가 존재하지 않습니다.");
        }
    }

    private void isCountPositive(int count) {
        if(count <=0){
            throw new IllegalStateException("상품의 숫자는 양수여만 합니다.");
        }
    }

    private OrderProduct saveOrderProduct(Product product, int count, int orderPrice, Order order) {
        OrderProduct orderProduct = OrderProduct.builder()
                .product(product)
                .count(count)
                .order(order)
                .orderPrice(orderPrice)
                .build();

        return orderProductRepository.save(orderProduct);
    }

    private Delivery saveDelivery(OrderCreateRequest request) {
        Address address = Address.builder()
                .city(request.getCity())
                .detailedAddress(request.getDetailedAddress())
                .zipcode(request.getZipcode())
                .build();

        Delivery delivery = Delivery.builder()
                .deliveryPrice(DELIVERY_PRICE)
                .status(DeliveryStatus.READY)
                .address(address)
                .build();

        return deliveryRepository.save(delivery);
    }
}
