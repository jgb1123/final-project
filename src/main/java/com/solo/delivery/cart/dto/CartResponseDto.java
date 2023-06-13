package com.solo.delivery.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDto {
    private Long cartId;
    private Integer itemCnt;
    private Long itemId;
    private Integer itemPrice;
    private String itemName;
}
