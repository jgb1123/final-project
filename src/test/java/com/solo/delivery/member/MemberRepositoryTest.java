package com.solo.delivery.member;

import com.solo.delivery.dummy.MemberDummy;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void saveMember() {
        //given
        Member member1 = MemberDummy.createMember1();
        //when
        Member savedMember = memberRepository.save(member1);
        //then
        assertThat(member1.getEmail()).isEqualTo(savedMember.getEmail());
    }

    @Test
    void findMember() {
        //given
        Member member1 = MemberDummy.createMember1();
        Member member2 = MemberDummy.createMember2();
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        //when
        Member foundMember1 = memberRepository.findById(savedMember1.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Can not find Member"));
        Member foundMember2 = memberRepository.findById(savedMember2.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Can not find Member"));
        assertThat(memberRepository.count()).isEqualTo(2);
        assertThat(foundMember1.getEmail()).isEqualTo(member1.getEmail());
        assertThat(foundMember2.getEmail()).isEqualTo(member2.getEmail());
    }

    @Test
    void updateMembers() {
        //given
        Member member1 = MemberDummy.createMember1();
        Member modifiedMember = MemberDummy.createMember2();
        Member savedMember = memberRepository.save(member1);
        Member foundMember = memberRepository.findById(savedMember.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Can not find member"));
        //when
        foundMember.changeInfo(modifiedMember);
        Member changedMember = memberRepository.findById(foundMember.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Can not find member"));
        //then
        assertThat(changedMember.getNickname()).isEqualTo(modifiedMember.getNickname());
    }

    @Test
    void deleteMember() {
        //given
        Member member1 = MemberDummy.createMember1();
        Member savedMember = memberRepository.save(member1);
        //when
        memberRepository.deleteById(savedMember.getMemberId());
        //then
        assertThat(memberRepository.findById(savedMember.getMemberId()).isPresent()).isFalse();
    }
}
