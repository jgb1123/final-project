package com.solo.delivery.store.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class StoreResponseDto {
    private Long storeId;
    private String storeName;
    private String address;
    private String phone;
    private Integer minimumOrderPrice;
    private String storeCategory;
    private Long memberId;

    @QueryProjection
    public StoreResponseDto(Long storeId, String storeName, String address, String phone, Integer minimumOrderPrice, String storeCategory, Long memberId) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.address = address;
        this.phone = phone;
        this.minimumOrderPrice = minimumOrderPrice;
        this.storeCategory = storeCategory;
        this.memberId = memberId;
    }
}
