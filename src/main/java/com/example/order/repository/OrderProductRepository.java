package com.example.order.repository;

import com.example.order.domain.OrderProduct;
import com.example.order.domain.order.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findByOrder(Order order);
}
