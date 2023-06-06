package com.solo.delivery.member;

import com.solo.delivery.dummy.MemberDummy;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.repository.MemberRepository;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.security.utils.CustomAuthorityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CustomAuthorityUtils customAuthorityUtils;


    @Test
    public void createMemberTest() {
        //given
        Member member1 = MemberDummy.createMember1();
        given(memberRepository.save(Mockito.any(Member.class)))
                .willReturn(member1);
        given(customAuthorityUtils.createRoles(Mockito.anyString()))
                .willReturn(List.of("ADMIN", "USER"));
        //when
        Member savedMember = memberService.createMember(member1);
        //then
        assertThat(member1.getEmail()).isEqualTo(savedMember.getEmail());
    }

    @Test
    public void findMemberTest() {
        //given
        Member member1 = MemberDummy.createMember1();
        given(memberRepository.findByEmail(Mockito.anyString()))
                .willReturn(Optional.of(member1));
        //when
        Member foundMember = memberService.findMember("hgd@gmail.com");
        //then
        assertThat(member1.getEmail()).isEqualTo(foundMember.getEmail());
    }

    @Test
    public void updateMemberTest() {
        //given
        Member member1 = MemberDummy.createMember1();
        Member modifiedMember = MemberDummy.createMember2();
        given(memberRepository.findByEmail(Mockito.anyString()))
                .willReturn(Optional.of(member1));
        //when
        Member updatedMember = memberService.updateMember("hgd@gmail.com", modifiedMember);

        //then
        assertThat(modifiedMember.getNickname()).isEqualTo(updatedMember.getNickname());
    }
}
