package com.example.order.service.product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.example.order.common.exception.ForbiddenException;
import com.example.order.controller.product.dto.ProductChangePriceRequest;
import com.example.order.controller.product.dto.ProductCreateRequest;
import com.example.order.controller.product.dto.ProductUpdateRequest;
import com.example.order.domain.member.Member;
import com.example.order.domain.member.MemberAuthority;
import com.example.order.domain.product.Product;
import com.example.order.domain.product.ProductHistoryType;
import com.example.order.domain.product.ProductType;
import com.example.order.repository.MemberRepository;
import com.example.order.repository.ProductHistoryRepository;
import com.example.order.repository.ProductRepository;
import com.example.order.service.product.response.ProductHistoryResponse;
import com.example.order.service.product.response.ProductResponse;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    @AfterEach
    void tearDown(){
        memberRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        productHistoryRepository.deleteAllInBatch();
    }

    @DisplayName("일반 권한 유저가 상품을 등록하면 Forbidden 예외가 터진다.")
    @Test
    void 일반유저_상품등록(){
      //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.NORMAL)
                .build();

        memberRepository.save(member);

        ProductCreateRequest request =ProductCreateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("고추장")
                .productType(ProductType.SEASONING)
                .price(8500)
                .build();

      //when //then
        assertThatThrownBy(() -> productService.createProduct(request, LocalDateTime.now()))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("MART 권한이 아닙니다.");
    }

    @DisplayName("상품 가격을 음수로 등록하면 ConstraintViolation 예외가 터진다.")
    @Test
    void 음수_가격으로_상품등록(){
        //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.MART)
                .build();

        memberRepository.save(member);

        ProductCreateRequest request =ProductCreateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("고추장")
                .productType(ProductType.SEASONING)
                .price(-8500)
                .build();

        //when //then
        assertThatThrownBy(() -> productService.createProduct(request, LocalDateTime.now()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("상품 가격을 0으로 등록하면 ConstraintViolation 예외가 터진다.")
    @Test
    void 빵원_가격으로_상품등록(){
        //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.MART)
                .build();

        memberRepository.save(member);

        ProductCreateRequest request =ProductCreateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("고추장")
                .productType(ProductType.SEASONING)
                .price(0)
                .build();

        //when //then
        assertThatThrownBy(() -> productService.createProduct(request, LocalDateTime.now()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("등록되지 않은 멤버가 상품을 등록하면 IllegalStateException 예외가 터진다.")
    @Test
    void 등록되지_않은_멤버가_상품등록(){
      //given
        ProductCreateRequest request =ProductCreateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("고추장")
                .productType(ProductType.SEASONING)
                .price(8500)
                .build();

      //when //then
        assertThatThrownBy(() -> productService.createProduct(request, LocalDateTime.now()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 멤버가 존재하지 않습니다.");
    }


    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
    @Test
    void 상품_생성(){
      //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.MART)
                .build();

        memberRepository.save(member);

        Product product = Product.builder()
                .productNumber("00001")
                .name("신라면")
                .price(700)
                .type(ProductType.NOODLE)
                .build();

        productRepository.save(product);


        ProductCreateRequest request =ProductCreateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("고추장")
                .productType(ProductType.SEASONING)
                .price(8500)
                .build();

      //when
        ProductResponse productResponse = productService.createProduct(request, LocalDateTime.now());

      //then
        assertThat(productResponse)
                .extracting("productNumber","name","type","price")
                .contains("00002","고추장",ProductType.SEASONING,8500);
    }

    @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품 번호는 00001이다.")
    @Test
    void 상품이_없을_때_등록하기(){
      //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.MART)
                .build();

        memberRepository.save(member);

        ProductCreateRequest request =ProductCreateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("고추장")
                .productType(ProductType.SEASONING)
                .price(8500)
                .build();

      //when
        ProductResponse productResponse = productService.createProduct(request, LocalDateTime.now());

      //then
        assertThat(productResponse)
                .extracting("productNumber","name","type","price")
                .contains("00001","고추장",ProductType.SEASONING,8500);
    }

    @DisplayName("일반 권한 유저가 상품을 수정하면 Forbidden 예외가 터진다.")
    @Test
    void 일반유저_상품수정(){
      //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.NORMAL)
                .build();

        memberRepository.save(member);

        Product product = Product.builder()
                .productNumber("00001")
                .name("신라면")
                .price(700)
                .type(ProductType.NOODLE)
                .build();

        productRepository.save(product);

        ProductUpdateRequest request =ProductUpdateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("고추장")
                .productType(ProductType.SEASONING)
                .price(8500)
                .build();

      //when //then
        assertThatThrownBy(() -> productService.changeProduct(1L, request, LocalDateTime.now()))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("MART 권한이 아닙니다.");
    }

    @DisplayName("상품이 없으면 수정 시 IllegalState 예외가 터진다.")
    @Test
    void 상품_없을_때_수정(){
      //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.MART)
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
                .name("컵누들")
                .price(1000)
                .type(ProductType.NOODLE)
                .build();

        productRepository.saveAll(List.of(product1,product2));

        ProductUpdateRequest request =ProductUpdateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("고추장")
                .productType(ProductType.SEASONING)
                .price(8500)
                .build();

      //when //then
        assertThatThrownBy(() -> productService.changeProduct(513L, request, LocalDateTime.now()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 상품이 존재하지 않습니다.");
    }

    @DisplayName("전체 상품을 수정할 때, 가격을 음수로 수정하면 IllegalState 예외가 터진다.")
    @Test
    void 상품의_가격을_음수로_수정(){
        //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.MART)
                .build();

        memberRepository.save(member);

        Product product = Product.builder()
                .productNumber("00001")
                .name("신라면")
                .price(1000)
                .type(ProductType.NOODLE)
                .build();

        productRepository.save(product);

        ProductUpdateRequest request =ProductUpdateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("고추장")
                .productType(ProductType.SEASONING)
                .price(-3500)
                .build();

        Long productId = productRepository.findByProductNumber(product.getProductNumber())
                .orElseThrow().getId();

        //when //then
        assertThatThrownBy(() -> productService.changeProduct(productId, request, LocalDateTime.now()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("상품의 가격에 음수가 올 수 없습니다.");
    }

    @DisplayName("상품을 수정한다.")
    @Test
    void 상품을_수정(){
        //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.MART)
                .build();

        memberRepository.save(member);

        Product product = Product.builder()
                .productNumber("00001")
                .name("신라면")
                .price(700)
                .type(ProductType.NOODLE)
                .build();

        productRepository.save(product);

        ProductUpdateRequest request =ProductUpdateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("고추장")
                .productType(ProductType.SEASONING)
                .price(1)
                .build();

        Long productId = productRepository.findByProductNumber(product.getProductNumber())
                .orElseThrow().getId();

        //when
        ProductResponse productResponse = productService.changeProduct(productId, request, LocalDateTime.now());

        // then
        assertThat(productResponse)
                .extracting("productNumber","name","type","price")
                .contains("00001","고추장",ProductType.SEASONING, 1);
    }

    @DisplayName("일반 권한 유저가 상품 가격을 수정하면 Forbidden 예외가 터진다.")
    @Test
    void 일반유저_상품가격_수정(){
      //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.NORMAL)
                .build();

        memberRepository.save(member);

        Product product = Product.builder()
                .productNumber("00001")
                .name("신라면")
                .price(700)
                .type(ProductType.NOODLE)
                .build();

        productRepository.save(product);

        ProductChangePriceRequest request =ProductChangePriceRequest.builder()
                .email("kjw1313@naver.com")
                .price(1000)
                .build();

        Long productId = productRepository.findByProductNumber(product.getProductNumber())
                .orElseThrow().getId();

      //when //then
        assertThatThrownBy(() -> productService.changePrice(productId, request, LocalDateTime.now()))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("MART 권한이 아닙니다.");
    }

    @DisplayName("상품 가격을 수정한다.")
    @Test
    void 상품가격_수정(){
      //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.MART)
                .build();

        memberRepository.save(member);

        Product product = Product.builder()
                .productNumber("00001")
                .name("신라면")
                .price(700)
                .type(ProductType.NOODLE)
                .build();

        productRepository.save(product);

        ProductChangePriceRequest request =ProductChangePriceRequest.builder()
                .email("kjw1313@naver.com")
                .price(1)
                .build();

        Long productId = productRepository.findByProductNumber(product.getProductNumber())
                .orElseThrow().getId();

      //when
        ProductResponse productResponse = productService.changePrice(productId, request, LocalDateTime.now());

      //then
        assertThat(productResponse)
                .extracting("productNumber","name","type","price")
                .contains("00001", "신라면", ProductType.NOODLE, 1);
    }

    @DisplayName("상품 가격을 음수로 수정한다.")
    @Test
    void 상품가격_음수로_수정(){
        //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.MART)
                .build();

        memberRepository.save(member);

        Product product = Product.builder()
                .productNumber("00001")
                .name("신라면")
                .price(700)
                .type(ProductType.NOODLE)
                .build();

        productRepository.save(product);

        ProductChangePriceRequest request =ProductChangePriceRequest.builder()
                .email("kjw1313@naver.com")
                .price(-1000)
                .build();

        Long productId = productRepository.findByProductNumber(product.getProductNumber())
                .orElseThrow().getId();

        //when //then
        assertThatThrownBy(() -> productService.changePrice(productId, request, LocalDateTime.now()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("상품의 가격에 음수가 올 수 없습니다.");
    }

    @DisplayName("일반 유저가 상품을 삭제하면 Forbidden 예외가 터진다.")
    @Test
    void 일반유저_상품삭제(){
      //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.NORMAL)
                .build();

        memberRepository.save(member);

        Product product = Product.builder()
                .productNumber("00001")
                .name("신라면")
                .price(700)
                .type(ProductType.NOODLE)
                .build();

        productRepository.save(product);

        Long productId = productRepository.findByProductNumber(product.getProductNumber())
                .orElseThrow().getId();

        String email = memberRepository.findByEmail(member.getEmail())
                .orElseThrow().getEmail();

      //when //then
        assertThatThrownBy(() -> productService.deleteProduct(productId, email, LocalDateTime.now()))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("MART 권한이 아닙니다.");
    }

    @DisplayName("특정 상품을 삭제한다.")
    @Test
    void 상품삭제(){
        //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.MART)
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
                .name("포스틱")
                .price(1200)
                .type(ProductType.SNACK)
                .build();

        Product product3 = Product.builder()
                .productNumber("00003")
                .name("감자깡")
                .price(1200)
                .type(ProductType.SNACK)
                .build();

        productRepository.saveAll(List.of(product1,product2,product3));

        Long productId = productRepository.findByProductNumber("00002")
                .orElseThrow().getId();

        String email = memberRepository.findByEmail(member.getEmail())
                .orElseThrow().getEmail();

        //when
        productService.deleteProduct(productId, email, LocalDateTime.now());

        Boolean isNullProduct = productRepository.findByProductNumber("00002")
                .isEmpty();
        Product foundedProduct1 = productRepository.findByProductNumber("00001")
                .orElseThrow();
        Product foundedProduct2 = productRepository.findByProductNumber("00003")
                .orElseThrow();

        //then
        assertThat(isNullProduct).isEqualTo(true);
        assertThat(foundedProduct1)
                .extracting("productNumber","name","type","price")
                .contains("00001", "신라면", ProductType.NOODLE, 700);
        assertThat(foundedProduct2)
                .extracting("productNumber","name","type","price")
                .contains("00003", "감자깡", ProductType.SNACK, 1200);
    }

    @DisplayName("상품을 등록 시, 특정 시점의 상품 기록을 조회.")
    @Test
    void 상품등록_시_특정시점_상품기록_조회(){
        //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.MART)
                .build();

        memberRepository.save(member);

        ProductCreateRequest request1 =ProductCreateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("고추장")
                .productType(ProductType.SEASONING)
                .price(8500)
                .build();

        ProductCreateRequest request2 =ProductCreateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("새우깡")
                .productType(ProductType.SNACK)
                .price(1200)
                .build();

        ProductCreateRequest request3 =ProductCreateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("포스틱")
                .productType(ProductType.SNACK)
                .price(1000)
                .build();

        ProductCreateRequest request4 =ProductCreateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("생생우동")
                .productType(ProductType.SNACK)
                .price(1000)
                .build();

        productService.createProduct(request1, LocalDateTime.of(2023,1,17,10,0,20));
        productService.createProduct(request2, LocalDateTime.of(2023,5,17,10,4,20));
        productService.createProduct(request3, LocalDateTime.of(2023,5,17,10,4,20));
        productService.createProduct(request4, LocalDateTime.of(2023,7,17,15,3,34));

        //when
        List<ProductHistoryResponse> productHistoryResponses = productService.getHistoryBasedOnTime(LocalDateTime.of(2023,5,17,10,4,20));

        //then
        Assertions.assertThat(productHistoryResponses).hasSize(2)
                .extracting("productNumber","name", "price", "historyType","type", "createdDate")
                .containsExactlyInAnyOrder(
                        tuple("00002","새우깡",1200, ProductHistoryType.CREATE,ProductType.SNACK,LocalDateTime.of(2023,5,17,10,4,20)),
                        tuple("00003","포스틱",1000,ProductHistoryType.CREATE,ProductType.SNACK,LocalDateTime.of(2023,5,17,10,4,20))
                );
    }

    @DisplayName("특정 시점의 상품 기록을 조회.")
    @Test
    void 특정시점_상품기록_조회(){
        //given
        Member member = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.MART)
                .build();

        memberRepository.save(member);

        ProductCreateRequest request1 =ProductCreateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("고추장")
                .productType(ProductType.SEASONING)
                .price(8500)
                .build();

        ProductCreateRequest request2 =ProductCreateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("새우깡")
                .productType(ProductType.SNACK)
                .price(1200)
                .build();

        productService.createProduct(request1, LocalDateTime.of(2023,1,17,10,0,20));
        productService.createProduct(request2, LocalDateTime.of(2023,5,17,10,4,20));

        ProductChangePriceRequest changedPrice =ProductChangePriceRequest.builder()
                .email("kjw1313@naver.com")
                .price(1000)
                .build();

        Long productId = productRepository.findByProductNumber("00002")
                .orElseThrow().getId();

        productService.changePrice(productId, changedPrice, LocalDateTime.of(2023,5,17,11,6,20));

        ProductCreateRequest request3 =ProductCreateRequest.builder()
                .email("kjw1313@naver.com")
                .productName("포스틱")
                .productType(ProductType.SNACK)
                .price(1000)
                .build();

        productService.createProduct(request3, LocalDateTime.of(2023,5,17,10,4,20));

        //when
        List<ProductHistoryResponse> productHistoryResponses = productService.getHistoryBasedOnTime(LocalDateTime.of(2023,5,17,11,6,20));

        //then
        Assertions.assertThat(productHistoryResponses).hasSize(1)
                .extracting("productNumber","name", "price", "historyType","type", "createdDate")
                .containsExactlyInAnyOrder(
                        tuple("00002","새우깡",1000, ProductHistoryType.UPDATE,ProductType.SNACK,LocalDateTime.of(2023,5,17,11,6,20))
                );
    }


}