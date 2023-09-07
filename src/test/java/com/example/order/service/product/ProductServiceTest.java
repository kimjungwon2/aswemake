package com.example.order.service.product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.example.order.common.exception.ForbiddenException;
import com.example.order.controller.product.dto.ProductChangePriceRequest;
import com.example.order.controller.product.dto.ProductCreateRequest;
import com.example.order.controller.product.dto.ProductUpdateRequest;
import com.example.order.domain.member.Member;
import com.example.order.domain.member.MemberAuthority;
import com.example.order.domain.product.Product;
import com.example.order.domain.product.ProductType;
import com.example.order.repository.MemberRepository;
import com.example.order.repository.ProductRepository;
import com.example.order.service.product.response.ProductResponse;
import java.util.List;
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

    @AfterEach
    void tearDown(){
        memberRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
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
        assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("MART 권한이 아닙니다.");
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
        assertThatThrownBy(() -> productService.createProduct(request))
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
        ProductResponse productResponse = productService.createProduct(request);

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
        ProductResponse productResponse = productService.createProduct(request);

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
        assertThatThrownBy(() -> productService.changeProduct(1L, request))
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
        assertThatThrownBy(() -> productService.changeProduct(513L, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 상품이 존재하지 않습니다.");
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
                .price(8500)
                .build();

        Long productId = productRepository.findByProductNumber(product.getProductNumber())
                .orElseThrow().getId();

        //when
        ProductResponse productResponse = productService.changeProduct(productId, request);

        // then
        assertThat(productResponse)
                .extracting("productNumber","name","type","price")
                .contains("00001","고추장",ProductType.SEASONING, 8500);
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
        assertThatThrownBy(() -> productService.changePrice(productId, request))
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
                .price(1000)
                .build();

        Long productId = productRepository.findByProductNumber(product.getProductNumber())
                .orElseThrow().getId();

      //when
        ProductResponse productResponse = productService.changePrice(productId, request);

      //then
        Product foundedProduct = productRepository.findByProductNumber("00001")
                .orElseThrow();


        assertThat(foundedProduct)
                .extracting("productNumber","name","type","price")
                .contains("00001", "신라면", ProductType.NOODLE, 1000);
    }
}