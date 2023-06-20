package com.solo.delivery.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberPatchDto {
    @NotBlank
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$",
            message = "휴대폰 번호는 10~11자의 숫자와 '-'로 구성되어야 합니다")
    private String phone;

    @NotBlank
    @Pattern(regexp = "^[가-힣|a-z|A-Z|0-9|]+$",
            message = "별명은 한글, 숫자, 영어만 가능합니다.")
    private String nickname;

    @NotBlank
    private String address;
}
