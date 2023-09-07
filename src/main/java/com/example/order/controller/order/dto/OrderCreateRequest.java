package com.example.order.controller.order.dto;

import com.example.order.domain.delivery.Delivery;
import com.example.order.domain.member.Member;
import com.example.order.domain.order.Order;
import com.example.order.domain.order.OrderStatus;
import java.util.HashMap;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

    @Email(message = "아이디가 Email 형식이 아닙니다.")
    @NotNull(message = "이메일을 입력하세요.")
    private String email;

    @NotBlank(message = "도시를 입력하세요.")
    private String city;

    @NotBlank(message = "상세 주소를 입력하세요.")
    private String detailedAddress;

    @NotBlank(message = "우편 번호를 입력하세요.")
    private String zipcode;

    @NotEmpty(message = "상품을 넣어주세요.")
    private HashMap<String, Integer> products;

    @Builder
    public OrderCreateRequest(
            String email,
            String city,
            String detailedAddress,
            String zipcode,
            HashMap<String, Integer> products
    ) {
        this.email = email;
        this.city = city;
        this.detailedAddress = detailedAddress;
        this.zipcode = zipcode;
        this.products = products;
    }

    public Order toEntity(int totalPrice){
        return Order.builder()
                .totalPrice(totalPrice)
                .status(OrderStatus.ORDER)
                .build();
    }
}
