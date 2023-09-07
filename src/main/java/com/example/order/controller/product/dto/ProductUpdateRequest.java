package com.example.order.controller.product.dto;

import com.example.order.domain.product.Product;
import com.example.order.domain.product.ProductType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateRequest {

    @NotNull(message = "로그인은 필수입니다.")
    private String email;

    private String productName;

    private ProductType productType;

    private Integer price;

    @Builder
    public ProductUpdateRequest(
            String email,
            String productName,
            ProductType productType,
            Integer price
    ) {
        this.email = email;
        this.productName = productName;
        this.productType = productType;
        this.price = price;
    }

    public Product toEntity(){
        return Product.builder()
                .type(productType)
                .name(productName)
                .price(price)
                .build();
    }
}