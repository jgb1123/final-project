package com.solo.delivery.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPatchDto {
    @Size(min = 2, max = 50, message = "상품 이름은 2자 이상 50자 이하이어야 합니다.")
    private String itemName;

    @Min(value = 100, message = "가격은 100원 이상이어야 합니다.")
    private Integer price;

    @PositiveOrZero
    private Integer stockCnt;

    @Size(min = 2, max = 3000, message = "내용은 2자 이상 3000자 이하이어야 합니다.")
    private String info;
}
