package com.solo.delivery.store.mapper;

import com.solo.delivery.store.dto.StorePatchDto;
import com.solo.delivery.store.dto.StorePostDto;
import com.solo.delivery.store.dto.StoreResponseDto;
import com.solo.delivery.store.entity.Store;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StoreMapper {
    public Store storePostDtoToStore(StorePostDto storePostDto) {
        return Store.builder()
                .storeName(storePostDto.getStoreName())
                .address(storePostDto.getAddress())
                .phone(storePostDto.getPhone())
                .minimumOrderPrice(storePostDto.getMinimumOrderPrice())
                .build();
    }

    public Store storePatchDtoToStore(StorePatchDto storePatchDto) {
        return Store.builder()
                .storeName(storePatchDto.getStoreName())
                .address(storePatchDto.getAddress())
                .phone(storePatchDto.getPhone())
                .minimumOrderPrice(storePatchDto.getMinimumOrderPrice())
                .build();
    }

    public StoreResponseDto storeToStoreResponseDto(Store store) {
        return StoreResponseDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .address(store.getAddress())
                .phone(store.getPhone())
                .minimumOrderPrice(store.getMinimumOrderPrice())
                .storeCategory(store.getStoreCategory().getStoreCategory())
                .build();
    }

    public List<StoreResponseDto> storesToStoreResponseDtos(List<Store> stores) {
        return stores.stream()
                .map(store -> storeToStoreResponseDto(store))
                .collect(Collectors.toList());
    }
}
