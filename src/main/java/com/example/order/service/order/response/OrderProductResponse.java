package com.example.order.service.order.response;

import com.example.order.domain.OrderProduct;
import com.example.order.domain.product.Product;
import com.example.order.service.product.response.ProductResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderProductResponse {
    private Long id;

    private ProductResponse productResponse;
    private Integer orderPrice;
    private Integer count;

    @Builder
    public OrderProductResponse(
            Long id,
            ProductResponse productResponse,
            Integer orderPrice,
            Integer count
    ) {
        this.id = id;
        this.productResponse = productResponse;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public static OrderProductResponse of(OrderProduct orderProduct){
        return OrderProductResponse.builder()
                .id(orderProduct.getId())
                .productResponse(ProductResponse.of(orderProduct.getProduct()))
                .count(orderProduct.getCount())
                .orderPrice(orderProduct.getOrderPrice())
                .build();
    }

}
