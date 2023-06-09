package com.solo.delivery.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponseDto {
    private Long memberId;
    private String name;
    private String email;
    private String phone;
    private String nickname;
    private String address;
}
