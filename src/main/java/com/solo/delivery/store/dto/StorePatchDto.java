package com.solo.delivery.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorePatchDto {
    @Size(min = 2, max = 50, message = "상점 이름은 2자 이상 50자 이하이어야 합니다.")
    private String storeName;

    private String address;

    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$",
            message = "휴대폰 번호는 10~11자의 숫자와 '-'로 구성되어야 합니다")
    private String phone;

    @PositiveOrZero
    private Integer minimumOrderPrice;

    @PositiveOrZero
    private Integer deliveryFee;

    private String storeCategoryId;

    @Positive
    private Long memberId;
}
