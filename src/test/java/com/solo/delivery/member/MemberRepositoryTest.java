package com.solo.delivery.member;

import com.solo.delivery.dummy.MemberDummy;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.repository.MemberRepository;
import com.solo.delivery.querydsl.config.QuerydslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("MemberRepository save")
    void saveMember() {
        //given
        Member member1 = MemberDummy.createMember1();
        //when
        Member savedMember = memberRepository.save(member1);
        //then
        assertThat(member1.getEmail()).isEqualTo(savedMember.getEmail());
    }

    @Test
    @DisplayName("MemberRepository findByEmail")
    void findMember() {
        //given
        Member member1 = MemberDummy.createMember1();
        Member member2 = MemberDummy.createMember2();
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        //when
        Member foundMember1 = memberRepository.findByEmail(savedMember1.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Can not find Member"));
        Member foundMember2 = memberRepository.findByEmail(savedMember2.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Can not find Member"));
        assertThat(memberRepository.count()).isEqualTo(2);
        assertThat(foundMember1.getEmail()).isEqualTo(member1.getEmail());
        assertThat(foundMember2.getEmail()).isEqualTo(member2.getEmail());
    }

    @Test
    @DisplayName("MemberRepository update")
    void updateMembers() {
        //given
        Member member1 = MemberDummy.createMember1();
        Member modifiedMember = MemberDummy.createMember2();
        Member savedMember = memberRepository.save(member1);
        Member foundMember = memberRepository.findById(savedMember.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Can not find member"));
        //when
        foundMember.changeMemberContent(modifiedMember);
        Member changedMember = memberRepository.findById(foundMember.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Can not find member"));
        //then
        assertThat(changedMember.getNickname()).isEqualTo(modifiedMember.getNickname());
    }

    @Test
    @DisplayName("MemberRepository delete")
    void deleteMember() {
        //given
        Member member1 = MemberDummy.createMember1();
        Member savedMember = memberRepository.save(member1);
        //when
        memberRepository.delete(savedMember);
        //then
        assertThat(memberRepository.findById(savedMember.getMemberId()).isPresent()).isFalse();
    }
}
