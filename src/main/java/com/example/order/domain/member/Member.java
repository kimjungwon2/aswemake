package com.example.order.domain.member;

import com.example.order.common.domain.BaseTimeEntity;
import com.example.order.domain.order.Order;
import com.sun.istack.NotNull;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(mappedBy="member")
    private List<Order> orders = new ArrayList<>();

    @Column(unique = true, nullable = false)
    @NotNull
    private String email;

    private String password;

    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    private MemberAuthority memberAuthority;

    @Builder
    public Member(
            Long id,
            String email,
            String password,
            String name,
            MemberAuthority memberAuthority
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.memberAuthority = memberAuthority;
    }
}
