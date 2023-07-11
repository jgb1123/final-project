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
        Member member1 = MemberDummy.createMember1();

        Member savedMember = memberRepository.save(member1);

        assertThat(member1.getEmail()).isEqualTo(savedMember.getEmail());
    }

    @Test
    @DisplayName("MemberRepository findByEmail")
    void findMember() {
        Member member1 = MemberDummy.createMember1();
        Member member2 = MemberDummy.createMember2();
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);

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
        Member member1 = MemberDummy.createMember1();
        Member modifiedMember = MemberDummy.createMember2();
        Member savedMember = memberRepository.save(member1);
        Member foundMember = memberRepository.findById(savedMember.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Can not find member"));

        foundMember.changeMemberContent(modifiedMember);
        Member changedMember = memberRepository.findById(foundMember.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Can not find member"));

        assertThat(changedMember.getNickname()).isEqualTo(modifiedMember.getNickname());
    }

    @Test
    @DisplayName("MemberRepository delete")
    void deleteMember() {
        Member member1 = MemberDummy.createMember1();
        Member savedMember = memberRepository.save(member1);

        memberRepository.delete(savedMember);

        assertThat(memberRepository.findById(savedMember.getMemberId()).isPresent()).isFalse();
    }
}
