package com.example.order.domain.member;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberAuthority {
    NORMAL("일반 권한"),
    MART("마트 권한");

    private final String text;
}
