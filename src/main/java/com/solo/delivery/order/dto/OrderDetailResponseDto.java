package com.solo.delivery.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponseDto {
    private Long orderDetailId;
    private Integer itemOrderCnt;
    private Long itemId;
    private String itemName;
    private Integer itemPrice;
}
