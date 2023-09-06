package com.example.order.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductType {

    SEASONING("조미료"),
    KIMCHI("김치류"),
    MARINE_PRODUCTS("수산물"),
    PROCESSED_GOODS("가공품"),
    DAIRY_PRODUCTS("낙농물"),
    SNACK("스낵류"),
    NOODLE("면류"),
    INSTANT("인스턴트 식품"),
    CEREAL("씨리얼"),
    CAN("통조림"),
    BEVERAGE("음료"),
    TEA("차류"),
    LIQUOR("주류"),
    DAILY_NECESSITY("일용품");

    private final String text;
}
