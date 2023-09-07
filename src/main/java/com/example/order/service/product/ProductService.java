package com.example.order.service.product;

import com.example.order.common.exception.ForbiddenException;
import com.example.order.controller.product.dto.ProductChangePriceRequest;
import com.example.order.controller.product.dto.ProductCreateRequest;
import com.example.order.controller.product.dto.ProductUpdateRequest;
import com.example.order.domain.member.Member;
import com.example.order.domain.member.MemberAuthority;
import com.example.order.domain.product.Product;
import com.example.order.repository.MemberRepository;
import com.example.order.repository.ProductRepository;
import com.example.order.service.product.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;


    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {

        String email = request.getEmail();

        checkMemberAuthority(email);

        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    @Transactional
    public ProductResponse changeProduct(Long productId, ProductUpdateRequest request) {
        String email = request.getEmail();
        checkMemberAuthority(email);

        Product changedProduct = request.toEntity();

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new IllegalStateException("해당 상품이 존재하지 않습니다."));

        product.changeProduct(changedProduct);

        return ProductResponse.of(product);
    }

    @Transactional
    public ProductResponse changePrice(Long productId, ProductChangePriceRequest request) {
        String email = request.getEmail();
        checkMemberAuthority(email);

        Product changedProduct = request.toEntity();

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new IllegalStateException("해당 상품이 존재하지 않습니다."));

        product.changeProduct(changedProduct);

        return ProductResponse.of(product);
    }

    @Transactional
    public void deleteProduct(Long productId, String email) {
        checkMemberAuthority(email);

        productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("해당 상품이 존재하지 않습니다."));

        productRepository.deleteById(productId);
    }


    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();

        if(latestProductNumber ==null){
            return "00001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt+1;

        return String.format("%05d",nextProductNumberInt);
    }


    private void checkMemberAuthority(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("해당 멤버가 존재하지 않습니다."));

        if(member.getMemberAuthority()==MemberAuthority.NORMAL){
            throw new ForbiddenException("MART 권한이 아닙니다.");
        }
    }
}
