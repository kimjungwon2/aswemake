package com.example.order.service.order;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.example.order.common.exception.ForbiddenException;
import com.example.order.controller.order.dto.OrderCreateRequest;
import com.example.order.domain.OrderProduct;
import com.example.order.domain.delivery.Delivery;
import com.example.order.domain.member.Member;
import com.example.order.domain.member.MemberAuthority;
import com.example.order.domain.order.Order;
import com.example.order.domain.order.OrderStatus;
import com.example.order.domain.product.Product;
import com.example.order.domain.product.ProductHistoryType;
import com.example.order.domain.product.ProductType;
import com.example.order.repository.DeliveryRepository;
import com.example.order.repository.MemberRepository;
import com.example.order.repository.OrderProductRepository;
import com.example.order.repository.OrderRepository;
import com.example.order.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;

    @AfterEach
    void tearDown(){
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        deliveryRepository.deleteAllInBatch();
    }

    @DisplayName("주문을 하면 배달비 + 상품의 총합 = 총 금액이 나온다.")
    @Test
    void 주문하면_총금액(){
      //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.NORMAL)
                .build();

        memberRepository.save(member);

        Product product1 = Product.builder()
                .productNumber("00001")
                .name("신라면")
                .price(700)
                .type(ProductType.NOODLE)
                .build();

        Product product2 = Product.builder()
                .productNumber("00002")
                .name("새우깡")
                .price(1200)
                .type(ProductType.SNACK)
                .build();

        productRepository.saveAll(List.of(product1,product2));

        HashMap<String, Integer> products = new HashMap<>();
        products.put("신라면",3);
        products.put("새우깡",1);

        OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
                .email(member.getEmail())
                .city("서울시")
                .detailedAddress("덕릉로 59바길 13")
                .zipcode("01468")
                .products(products)
                .build();

      //when
        Integer totalPrice= orderService.createOrder(orderCreateRequest);

      //then
        assertThat(totalPrice).isEqualTo(5800);
    }

    @DisplayName("주문을 하면 Delivery, Order, OrderProduct 테이블의 데이터가 삽입된다.")
    @Test
    void 주문_시_다양한_테이블에서_데이터_삽입(){
      //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.NORMAL)
                .build();

        memberRepository.save(member);

        Product product1 = Product.builder()
                .productNumber("00001")
                .name("신라면")
                .price(700)
                .type(ProductType.NOODLE)
                .build();

        Product product2 = Product.builder()
                .productNumber("00002")
                .name("새우깡")
                .price(1200)
                .type(ProductType.SNACK)
                .build();

        Product product3 = Product.builder()
                .productNumber("00002")
                .name("포스틱")
                .price(1100)
                .type(ProductType.SNACK)
                .build();

        productRepository.saveAll(List.of(product1,product2,product3));

        HashMap<String, Integer> products = new HashMap<>();
        products.put("신라면",3);
        products.put("새우깡",1);
        products.put("포스틱",5);

        OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
                .email(member.getEmail())
                .city("서울시")
                .detailedAddress("덕릉로 59바길 13")
                .zipcode("01468")
                .products(products)
                .build();

      //when
        orderService.createOrder(orderCreateRequest);

      //then
        List<Delivery> deliveries = deliveryRepository.findAll();
        List<Order> orders = orderRepository.findAll();
        List<OrderProduct> orderProducts = orderProductRepository.findAll();

        Assertions.assertThat(deliveries).hasSize(1)
                .extracting("address","deliveryPrice","status")
                .containsExactlyInAnyOrder(
                        tuple(deliveries.get(0).getAddress(),deliveries.get(0).getDeliveryPrice(),deliveries.get(0).getStatus())
                );

        Assertions.assertThat(orders).hasSize(1)
                .extracting("member","delivery","totalPrice","status")
                .containsExactlyInAnyOrder(
                        tuple(orders.get(0).getMember(),
                                orders.get(0).getDelivery(),
                                orders.get(0).getTotalPrice(),
                                OrderStatus.ORDER)
                );

        Assertions.assertThat(orderProducts).hasSize(3)
                .extracting("orderPrice","count")
                .containsExactlyInAnyOrder(
                        tuple( 2100, 3),
                        tuple( 1200, 1),
                        tuple( 5500, 5)
                );
    }

    @DisplayName("상품이 없는 걸 주문하면 IllegalState 예외가 터진다.")
    @Test
    void 상품이_없는걸_주문하면_예외발생(){
        //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.NORMAL)
                .build();

        memberRepository.save(member);

        Product product1 = Product.builder()
                .productNumber("00001")
                .name("신라면")
                .price(700)
                .type(ProductType.NOODLE)
                .build();

        Product product2 = Product.builder()
                .productNumber("00002")
                .name("새우깡")
                .price(1200)
                .type(ProductType.SNACK)
                .build();

        Product product3 = Product.builder()
                .productNumber("00002")
                .name("포스틱")
                .price(1100)
                .type(ProductType.SNACK)
                .build();

        productRepository.saveAll(List.of(product1,product3));

        HashMap<String, Integer> products = new HashMap<>();
        products.put("신라면",3);
        products.put("새우깡",1);
        products.put("포스틱",5);

        OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
                .email(member.getEmail())
                .city("서울시")
                .detailedAddress("덕릉로 59바길 13")
                .zipcode("01468")
                .products(products)
                .build();

        //when then
        assertThatThrownBy(() -> orderService.createOrder(orderCreateRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 상품이 존재하지 않습니다.");
    }
}