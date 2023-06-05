package com.solo.delivery.member.mapper;

import com.solo.delivery.member.dto.MemberPatchDto;
import com.solo.delivery.member.dto.MemberResponseDto;
import com.solo.delivery.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    public Member memberPatchDtoToMember(MemberPatchDto memberPatchDto) {
        return Member.builder()
                .phone(memberPatchDto.getPhone())
                .nickname(memberPatchDto.getNickname())
                .build();
    }

    public MemberResponseDto memberToMemberResponseDto(Member member) {
        return MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .build();
    }
}
