package com.example.order.domain.product;


import com.example.order.common.domain.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Positive;
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

    @Positive
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

    public void changeProduct(Product product){
        isPriceNegative(product);
        this.name = product.getName();
        this.type = product.getType();
        this.price = product.getPrice();
    }



    public void changePrice(Product product) {
        isPriceNegative(product);
        this.price = product.getPrice();
    }

    private void isPriceNegative(Product product) {
        if(product.getPrice() < 0){
            throw new IllegalStateException("가격에 음수가 올 수 없습니다.");
        }
    }
}
