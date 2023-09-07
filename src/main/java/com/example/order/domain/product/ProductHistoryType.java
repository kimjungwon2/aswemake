package com.example.order.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductHistoryType {

    CREATE("생성"),
    DELETE("삭제"),
    UPDATE("수정");

    private final String text;
}
