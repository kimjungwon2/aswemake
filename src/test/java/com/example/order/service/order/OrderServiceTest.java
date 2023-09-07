package com.example.order.service.order;


import com.example.order.controller.order.dto.OrderCreateRequest;
import com.example.order.domain.member.Member;
import com.example.order.domain.member.MemberAuthority;
import com.example.order.domain.order.Order;
import com.example.order.domain.product.Product;
import com.example.order.domain.product.ProductType;
import com.example.order.repository.DeliveryRepository;
import com.example.order.repository.MemberRepository;
import com.example.order.repository.OrderProductRepository;
import com.example.order.repository.OrderRepository;
import com.example.order.repository.ProductRepository;
import com.example.order.service.order.response.OrderResponse;
import java.util.HashMap;
import java.util.List;
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
        orderRepository.deleteAllInBatch();
        deliveryRepository.deleteAllInBatch();
        orderProductRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @DisplayName("주문을 한다.")
    @Test
    void 주문하기(){
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
        orderService.createOrder(orderCreateRequest);

        Order order = orderRepository.findById(1L)
                .orElseThrow(()-> new IllegalStateException("주문이 존재하지 않습니다"));

        System.out.println(order);

      //then

    }
}