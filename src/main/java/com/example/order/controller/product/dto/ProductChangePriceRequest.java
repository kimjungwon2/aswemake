package com.example.order.controller.product.dto;

import com.example.order.domain.product.Product;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductChangePriceRequest {
    @NotNull(message = "로그인은 필수입니다.")
    private String email;

    private Integer price;

    @Builder
    public ProductChangePriceRequest(String email, Integer price) {
        this.email = email;
        this.price = price;
    }

    public Product toEntity(){
        return Product.builder()
                .price(price)
                .build();
    }
}
