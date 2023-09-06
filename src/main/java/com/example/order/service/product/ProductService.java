package com.example.order.service.product;

import com.example.order.common.exception.ForbiddenException;
import com.example.order.controller.product.dto.ProductCreateRequest;
import com.example.order.domain.member.Member;
import com.example.order.domain.member.MemberAuthority;
import com.example.order.domain.product.Product;
import com.example.order.repository.MemberRepository;
import com.example.order.repository.ProductRepository;
import com.example.order.service.product.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;


    public ProductResponse createProduct(ProductCreateRequest request) {

        String email = request.getEmail();

        checkMemberAuthority(email);

        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
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
