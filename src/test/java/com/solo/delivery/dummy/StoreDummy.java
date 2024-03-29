package com.solo.delivery.dummy;

import com.solo.delivery.store.dto.StorePatchDto;
import com.solo.delivery.store.dto.StorePostDto;
import com.solo.delivery.store.dto.StoreResponseDto;
import com.solo.delivery.store.entity.Store;
import com.solo.delivery.store.entity.StoreCategory;

public interface StoreDummy {
    static Store createStore1() {
        return Store.builder()
                .storeId(1L)
                .storeName("대만각")
                .address("서울시 구로구 고척동 111-11")
                .phone("010-1111-2222")
                .minimumOrderPrice(10000)
                .memberId(1L)
                .starAvg(4.5)
                .totalOrderCnt(14)
                .deliveryFee(3000)
                .build();
    }

    static Store createStore2() {
        return Store.builder()
                .storeId(2L)
                .storeName("백년아구찜")
                .address("서울시 구로구 고척동 111-12")
                .phone("010-2222-3333")
                .minimumOrderPrice(20000)
                .memberId(2L)
                .starAvg(4.8)
                .totalOrderCnt(421)
                .deliveryFee(2000)
                .build();
    }

    static StoreCategory createStoreCategory() {
        return StoreCategory.builder()
                .storeCategoryId("001")
                .storeCategory("한식")
                .build();
    }

    static StorePostDto createPostDto() {
        return StorePostDto.builder()
                .storeName("대만각")
                .address("서울시 구로구 고척동 111-11")
                .phone("010-1111-2222")
                .minimumOrderPrice(10000)
                .storeCategoryId("002")
                .memberId(1L)
                .deliveryFee(3000)
                .build();
    }

    static StorePatchDto createPatchDto() {
        return StorePatchDto.builder()
                .storeName("대만각")
                .address("서울시 구로구 고척동 111-11")
                .phone("010-1111-3333")
                .minimumOrderPrice(20000)
                .storeCategoryId("002")
                .memberId(1L)
                .deliveryFee(2000)
                .build();
    }

    static StoreResponseDto createResponseDto1() {
        return StoreResponseDto.builder()
                .storeId(1L)
                .storeName("대만각")
                .address("서울시 구로구 고척동 111-11")
                .phone("010-1111-2222")
                .minimumOrderPrice(10000)
                .storeCategory("중국집")
                .memberId(1L)
                .starAvg(4.5)
                .totalOrderCnt(14)
                .deliveryFee(3000)
                .build();
    }

    static StoreResponseDto createResponseDto2() {
        return StoreResponseDto.builder()
                .storeId(2L)
                .storeName("백년아구찜")
                .address("서울시 구로구 고척동 111-12")
                .phone("010-2222-3333")
                .minimumOrderPrice(20000)
                .storeCategory("한식")
                .memberId(2L)
                .starAvg(4.8)
                .totalOrderCnt(421)
                .deliveryFee(2000)
                .build();
    }
}
