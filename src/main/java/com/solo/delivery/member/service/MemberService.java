package com.solo.delivery.member.service;

import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.exception.ExceptionCode;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.repository.MemberRepository;
import com.solo.delivery.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils customAuthorityUtils;

    public Member createMember(Member member) {
        List<String> roles = customAuthorityUtils.createRoles(member.getEmail());
        member.changeRoles(roles);
        return memberRepository.save(member);
    }

    public Member findMember(String email) {
        return findVerifiedMember(email);
    }

    public Member updateMember(String email, Member modifiedMember) {
        Member foundMember = findVerifiedMember(email);
        foundMember.changeMemberContent(modifiedMember);
        return foundMember;
    }

    public Member authorizeRole(String role, Long memberId) {
        Member.Role.checkRole(role);
        Member foundMember = findVerifiedMemberById(memberId);
        List<String> roles = foundMember.getRoles();
        if(roles.contains(role)) {
            throw new BusinessLogicException(ExceptionCode.ROLE_CANNOT_CHANGE);
        }
        roles.add(role);
        return foundMember;
    }

    public void deleteMember(String email) {
        Member foundMember = findVerifiedMember(email);
        memberRepository.delete(foundMember);
    }

    public Member findVerifiedMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    public Member findVerifiedMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    public boolean existsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.isPresent();
    }

}
