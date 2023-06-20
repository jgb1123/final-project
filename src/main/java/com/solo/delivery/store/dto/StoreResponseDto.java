package com.solo.delivery.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponseDto {
    private Long storeId;
    private String storeName;
    private String address;
    private String phone;
    private Integer minimumOrderPrice;
    private String storeCategory;
    private Long memberId;
}
