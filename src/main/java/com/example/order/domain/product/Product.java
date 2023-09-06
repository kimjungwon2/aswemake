package com.example.order.domain.product;


import com.example.order.common.domain.BaseEntity;
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
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    private Integer price;

    @Builder
    public Product(
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
}
