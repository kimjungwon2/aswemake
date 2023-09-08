package com.example.order.repository;

import com.example.order.domain.member.Member;
import com.example.order.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByMember(Member member);
}
