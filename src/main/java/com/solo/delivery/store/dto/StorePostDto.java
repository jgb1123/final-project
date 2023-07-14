package com.solo.delivery.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorePostDto {
    @NotBlank
    @Size(min = 2, max = 50, message = "상점 이름은 2자 이상 50자 이하이어야 합니다.")
    private String storeName;

    @NotBlank
    private String address;

    @NotBlank
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$",
            message = "휴대폰 번호는 10~11자의 숫자와 '-'로 구성되어야 합니다")
    private String phone;

    @NotNull
    @PositiveOrZero
    private Integer minimumOrderPrice;

    @NotNull
    @PositiveOrZero
    private Integer deliveryFee;

    @NotBlank
    private String storeCategoryId;

    @NotNull
    @Positive
    private Long memberId;
}
