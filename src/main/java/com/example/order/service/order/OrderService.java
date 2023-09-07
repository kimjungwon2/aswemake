package com.example.order.service.order;

import com.example.order.controller.order.dto.OrderCreateRequest;
import com.example.order.domain.OrderProduct;
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
import com.example.order.service.order.response.OrderResponse;
import java.util.HashMap;
import java.util.List;
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

    private static final Integer DELIVERY_PRICE = 2500;

    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new IllegalStateException("해당 이메일의 멤버가 존재하지 않습니다."));

        Delivery delivery = saveDelivery(request);

        int totalProductPrice = getTotalOrderProductPrice(request);

        Order order = saveOrder(request, member, delivery, totalProductPrice);

        return OrderResponse.of(order);
    }

    private Order saveOrder(
            OrderCreateRequest request,
            Member member,
            Delivery delivery,
            int totalProductPrice
    ) {
        Order order = request.toEntity(totalProductPrice);
        orderRepository.save(order);

        order.setDelivery(delivery);
        order.setMember(member);

        List<OrderProduct> orderProducts = orderProductRepository.findByOrder(order);

        for(OrderProduct orderProduct : orderProducts){
            order.addOrderProduct(orderProduct);
        }

        return order;
    }

    private int getTotalOrderProductPrice(OrderCreateRequest request) {
        int totalProductPrice = 0;

        HashMap<String, Integer> products = request.getProducts();
        Set<String> productNames = products.keySet();

        for(String productName: productNames){
            Product product = productRepository.findByName(productName)
                       .orElseThrow(() -> new IllegalStateException("해당 상품이 존재하지 않습니다."));

            int count = products.get(productName);
            int orderPrice = count * product.getPrice();

            saveOrderProduct(product, count, orderPrice);

            totalProductPrice += orderPrice;
        }

        return totalProductPrice;
    }

    private void saveOrderProduct(Product product, int count, int orderPrice) {
        OrderProduct orderProduct = OrderProduct.builder()
                .product(product)
                .count(count)
                .orderPrice(orderPrice)
                .build();

        orderProductRepository.save(orderProduct);
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
