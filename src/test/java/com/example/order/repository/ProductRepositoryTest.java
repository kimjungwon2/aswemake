package com.example.order.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.order.domain.product.Product;
import com.example.order.domain.product.ProductType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;


    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
    @Test
    void 상품번호_조회_시_상품이_없으면_null(){

      //when
        String latestProductNumber = productRepository.findLatestProductNumber();

      //then
        assertThat(latestProductNumber).isNull();
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
    @Test
    void 마지막_상품번호_조회(){
      //given

        Product product1 = Product.builder()
                .productNumber("00001")
                .name("신라면")
                .price(700)
                .type(ProductType.NOODLE)
                .build();

        Product product2 = Product.builder()
                .productNumber("00002")
                .name("순창 고추장")
                .price(8500)
                .type(ProductType.SEASONING)
                .build();

        Product product3 = Product.builder()
                .productNumber("00003")
                .name("핫바")
                .price(1500)
                .type(ProductType.INSTANT)
                .build();

        productRepository.saveAll(List.of(product1,product2,product3));

      //when
        String latestProductNumber = productRepository.findLatestProductNumber();

      //then
        assertThat(latestProductNumber).isEqualTo("00003");
    }
}