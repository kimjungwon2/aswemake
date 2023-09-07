package com.example.order.service.product.response;

import com.example.order.domain.product.ProductHistory;
import com.example.order.domain.product.ProductHistoryType;
import com.example.order.domain.product.ProductType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductHistoryResponse {
    private Long id;

    private LocalDateTime createdDate;

    private ProductHistoryType historyType;

    private String productNumber;

    private String name;

    private ProductType type;

    private Integer price;

    @Builder
    public ProductHistoryResponse(
            Long id,
            LocalDateTime createdDate,
            ProductHistoryType historyType,
            String productNumber,
            String name,
            ProductType type,
            Integer price
    ) {
        this.id = id;
        this.createdDate = createdDate;
        this.historyType = historyType;
        this.productNumber = productNumber;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public static ProductHistoryResponse of(ProductHistory productHistory){
        return ProductHistoryResponse.builder()
                .id(productHistory.getId())
                .createdDate(productHistory.getCreatedDate())
                .historyType(productHistory.getHistoryType())
                .productNumber(productHistory.getProductNumber())
                .name(productHistory.getName())
                .type(productHistory.getType())
                .price(productHistory.getPrice())
                .build();
    }
}
