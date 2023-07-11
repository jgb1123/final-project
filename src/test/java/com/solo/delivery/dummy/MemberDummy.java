package com.solo.delivery.dummy;

import com.solo.delivery.member.dto.MemberPatchDto;
import com.solo.delivery.member.dto.MemberResponseDto;
import com.solo.delivery.member.entity.Member;

import java.util.ArrayList;
import java.util.List;

public interface MemberDummy {
    static Member createMember1() {
        return Member.builder()
                .memberId(1L)
                .name("홍길동")
                .email("hgd@gmail.com")
                .phone("010-1234-5678")
                .nickname("hgd123")
                .address("서울시 구로구 고척동 111-11 11호")
                .roles(new ArrayList<>(List.of("USER")))
                .build();
    }

    static Member createMember2() {
        return Member.builder()
                .memberId(2L)
                .name("이순신")
                .email("lss@gmail.com")
                .phone("010-8765-4321")
                .nickname("lss123")
                .address("서울시 구로구 고척동 222-22 22호")
                .roles(new ArrayList<>(List.of("USER")))
                .build();
    }

    static MemberPatchDto createPatchDto() {
        return MemberPatchDto.builder()
                .phone("010-1234-5678")
                .nickname("hgd123")
                .address("서울시 구로구 고척동 111-11 11호")
                .build();
    }

    static MemberResponseDto createResponseDto1() {
        return MemberResponseDto.builder()
                .memberId(1L)
                .name("홍길동")
                .email("hgd@gmail.com")
                .phone("010-1234-5678")
                .nickname("hgd123")
                .address("서울시 구로구 고척동 11-11 11호")
                .build();
    }
}
