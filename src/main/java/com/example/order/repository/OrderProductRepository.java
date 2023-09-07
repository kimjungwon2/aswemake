package com.example.order.repository;

import com.example.order.domain.OrderProduct;
import com.example.order.domain.product.Product;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
