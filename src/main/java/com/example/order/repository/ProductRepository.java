package com.example.order.repository;

import com.example.order.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select p.product_number from product p order by id desc limit 1",nativeQuery = true)
    String findLatestProductNumber();
}
