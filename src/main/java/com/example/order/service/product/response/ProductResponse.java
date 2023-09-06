package com.example.order.service.product.response;

import com.example.order.domain.product.Product;
import com.example.order.domain.product.ProductType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {

    private Long id;

    private String productNumber;

    private String name;

    private ProductType type;

    private Integer price;

    @Builder
    public ProductResponse(
            Long id,
            String productNumber,
            String name,
            ProductType type,
            Integer price
    ) {
        this.id = id;
        this.productNumber = productNumber;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public static ProductResponse of(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .productNumber(product.getProductNumber())
                .name(product.getName())
                .type(product.getType())
                .price(product.getPrice())
                .build();
    }
}
