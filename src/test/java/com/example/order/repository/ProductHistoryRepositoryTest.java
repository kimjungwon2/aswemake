package com.example.order.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.example.order.domain.product.ProductHistory;
import com.example.order.domain.product.ProductHistoryType;
import com.example.order.domain.product.ProductType;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductHistoryRepositoryTest {

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    @DisplayName("특정 시간의 상품 기록을 조회한다.")
    @Test
    void 상품기록_조회(){
      //given
        ProductHistory productHistory1 = ProductHistory.builder()
                .productNumber("00001")
                .name("신라면")
                .price(700)
                .historyType(ProductHistoryType.CREATE)
                .type(ProductType.NOODLE)
                .createdDate(LocalDateTime.of(2023,1,17,10,0,0))
                .build();

        ProductHistory productHistory2 = ProductHistory.builder()
                .productNumber("00002")
                .name("순창 고추장")
                .price(8500)
                .type(ProductType.SEASONING)
                .historyType(ProductHistoryType.CREATE)
                .createdDate(LocalDateTime.of(2023,1,17,10,0,20))
                .build();

        ProductHistory productHistory3 = ProductHistory.builder()
                .productNumber("00003")
                .name("핫바")
                .price(1500)
                .historyType(ProductHistoryType.CREATE)
                .type(ProductType.INSTANT)
                .createdDate(LocalDateTime.of(2023,1,17,10,0,20))
                .build();

        ProductHistory productHistory4 = ProductHistory.builder()
                .productNumber("00002")
                .name("순창 고추장")
                .price(7500)
                .historyType(ProductHistoryType.UPDATE)
                .type(ProductType.NOODLE)
                .createdDate(LocalDateTime.of(2023,1,17,14,0,5))
                .build();

        productHistoryRepository.saveAll(List.of(productHistory1,productHistory2,productHistory3,productHistory4));

      //when
        List<ProductHistory> productHistories = productHistoryRepository
                .findByCreatedDate(LocalDateTime.of(2023,1,17,10,0,20));

      //then
        assertThat(productHistories).hasSize(2)
                .extracting("productNumber","name", "price", "historyType","type", "createdDate")
                .containsExactlyInAnyOrder(
                    tuple("00002","순창 고추장",8500,ProductHistoryType.CREATE,ProductType.SEASONING,LocalDateTime.of(2023,1,17,10,0,20)),
                        tuple("00003","핫바",1500,ProductHistoryType.CREATE,ProductType.INSTANT,LocalDateTime.of(2023,1,17,10,0,20))
                );

    }

}