package com.example.order.controller.product.dto;

import com.example.order.domain.product.Product;
import com.example.order.domain.product.ProductHistory;
import com.example.order.domain.product.ProductHistoryType;
import com.example.order.domain.product.ProductType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotNull(message = "로그인은 필수입니다.")
    private String email;

    @NotBlank(message = "상품 이름은 필수입니다.")
    private String productName;

    @NotNull(message = "상품 종류는 필수입니다.")
    private ProductType productType;

    @Positive(message = "상품 가격은 양수여야 합니다.")
    private Integer price;


    @Builder
    public ProductCreateRequest(
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

    public Product toEntity(String productNumber){
        return Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .name(productName)
                .price(price)
                .build();
    }

    public ProductHistory toHistoryEntity(Product product){
        return ProductHistory.builder()
                .productNumber(product.getProductNumber())
                .historyType(ProductHistoryType.CREATE)
                .type(product.getType())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
