package com.solo.delivery.member;

import com.solo.delivery.dummy.MemberDummy;
import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.repository.MemberRepository;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.security.utils.CustomAuthorityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("MemberService createMember")
    public void createMemberTest() {
        Member member1 = MemberDummy.createMember1();
        given(memberRepository.save(Mockito.any(Member.class)))
                .willReturn(member1);
        given(customAuthorityUtils.createRoles(Mockito.anyString()))
                .willReturn(List.of("ADMIN", "USER"));

        Member savedMember = memberService.createMember(member1);

        assertThat(member1.getEmail()).isEqualTo(savedMember.getEmail());
    }

    @Test
    @DisplayName("MemberService findMember")
    public void findMemberTest() {
        Member member1 = MemberDummy.createMember1();
        given(memberRepository.findByEmail(Mockito.anyString()))
                .willReturn(Optional.of(member1));

        Member foundMember = memberService.findMember("hgd@gmail.com");

        assertThat(member1.getEmail()).isEqualTo(foundMember.getEmail());
    }

    @Test
    @DisplayName("MemberService updateMember")
    public void updateMemberTest() {
        Member member1 = MemberDummy.createMember1();
        Member modifiedMember = MemberDummy.createMember2();
        given(memberRepository.findByEmail(Mockito.anyString()))
                .willReturn(Optional.of(member1));

        Member updatedMember = memberService.updateMember("hgd@gmail.com", modifiedMember);

        assertThat(modifiedMember.getNickname()).isEqualTo(updatedMember.getNickname());
    }

    @Test
    @DisplayName("MemberService authorizeRole")
    public void authorizeRoleTest() {
        Member member = MemberDummy.createMember1();
        given(memberRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(member));

        memberService.authorizeRole("SELLER", 1L);

        assertThat(member.getRoles()).contains("SELLER");
    }

    @Test
    @DisplayName("MemberService authorizeRole 유효하지 않은 role")
    public void authorizeRoleWithInvalidRoleTest() {
        Member member = MemberDummy.createMember1();

        Assertions.assertThrows(BusinessLogicException.class, () -> memberService.authorizeRole("ABCD", 1L));
    }

    @Test
    @DisplayName("MemberService authorizeRole 이미 존재하는 role")
    public void authorizeRoleWithExistTest() {
        Member member = MemberDummy.createMember1();
        given(memberRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(member));

        Assertions.assertThrows(BusinessLogicException.class, () -> memberService.authorizeRole("USER", 1L));
    }
}
