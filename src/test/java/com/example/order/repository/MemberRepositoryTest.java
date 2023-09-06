package com.example.order.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.order.domain.member.Member;
import com.example.order.domain.member.MemberAuthority;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("이메일로 멤버를 찾는다.")
    @Test
    void 이메일로_조회(){
      //given
        Member member1 = Member.builder()
                .email("kjw1313@naver.com")
                .name("김정원")
                .memberAuthority(MemberAuthority.NORMAL)
                .build();

        Member member2 = Member.builder()
                .email("kis6@naver.com")
                .name("희영도")
                .memberAuthority(MemberAuthority.NORMAL)
                .build();

        Member member3 = Member.builder()
                .email("sakch25@naver.com")
                .name("이희도")
                .memberAuthority(MemberAuthority.NORMAL)
                .build();

        memberRepository.saveAll(List.of(member1,member2,member3));

      //when
        Member foundedMember = memberRepository.findByEmail("kis6@naver.com").orElse(null);

      //then
        assertThat(foundedMember)
                .extracting("name","memberAuthority")
                .contains("희영도",MemberAuthority.NORMAL);
    }

}