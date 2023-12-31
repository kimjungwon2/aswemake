package com.example.order.domain.product;

import com.example.order.common.domain.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProductHistoryType historyType;

    private String productNumber;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    private Integer price;

    private LocalDateTime createdDate;

    @Builder
    public ProductHistory(
            Long id,
            ProductHistoryType historyType,
            String productNumber,
            String name,
            ProductType type,
            Integer price,
            LocalDateTime createdDate
    ) {
        this.id = id;
        this.historyType = historyType;
        this.productNumber = productNumber;
        this.name = name;
        this.type = type;
        this.price = price;
        this.createdDate = createdDate;
    }
}
