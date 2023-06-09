package com.solo.delivery.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPatchDto {
    private String itemName;
    private Integer price;
    private Integer stockCnt;
    private String info;
}
