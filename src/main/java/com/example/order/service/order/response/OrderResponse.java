package com.example.order.service.order.response;

import com.example.order.domain.member.Member;
import com.example.order.domain.order.Order;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderResponse {

    private Long id;

    private Member member;

    private List<OrderProductResponse> orderProduct;

    private DeliveryResponse deliveryResponse;

    private Integer totalPrice;

    @Builder
    public OrderResponse(
            Long id,
            Member member,
            List<OrderProductResponse> orderProduct,
            DeliveryResponse deliveryResponse,
            Integer totalPrice
    ) {
        this.id = id;
        this.member = member;
        this.orderProduct = orderProduct;
        this.deliveryResponse = deliveryResponse;
        this.totalPrice = totalPrice;
    }

    public static OrderResponse of(Order order){
        return OrderResponse.builder()
                .id(order.getId())
                .member(order.getMember())
                .deliveryResponse(DeliveryResponse.of(order.getDelivery()))
                .orderProduct(
                        order.getOrderProduct().stream()
                        .map(orderProduct-> OrderProductResponse.of(orderProduct))
                        .collect(Collectors.toList())
                )
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
