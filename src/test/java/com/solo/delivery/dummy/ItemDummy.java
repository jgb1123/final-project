package com.solo.delivery.dummy;

import com.solo.delivery.item.dto.ItemPatchDto;
import com.solo.delivery.item.dto.ItemPostDto;
import com.solo.delivery.item.dto.ItemResponseDto;
import com.solo.delivery.item.entity.Item;

public interface ItemDummy {
    static Item createItem1() {
        return Item.builder()
                .itemId(1L)
                .itemName("김치볶음밥")
                .price(8000)
                .soldCnt(0)
                .stockCnt(10)
                .info("김치볶음밥입니다.")
                .build();
    }

    static Item createItem2() {
        return Item.builder()
                .itemId(2L)
                .itemName("짜장면")
                .price(6000)
                .soldCnt(0)
                .stockCnt(20)
                .info("짜장면입니다")
                .build();
    }

    static ItemPostDto createPostDto() {
        return ItemPostDto.builder()
                .itemName("김치볶음밥")
                .price(8000)
                .stockCnt(10)
                .info("김치볶음밥입니다")
                .build();
    }

    static ItemPatchDto createPatchDto() {
        return ItemPatchDto.builder()
                .itemName("야채볶음밥")
                .price(7000)
                .stockCnt(10)
                .info("야채볶음밥입니다")
                .build();
    }

    static ItemResponseDto createResponseDto1() {
        return ItemResponseDto.builder()
                .itemId(1L)
                .itemName("김치볶음밥")
                .price(8000)
                .soldCnt(0)
                .stockCnt(10)
                .info("김치볶음밥입니다")
                .build();
    }

    static ItemResponseDto createResponseDto2() {
        return ItemResponseDto.builder()
                .itemId(2L)
                .itemName("짜장면")
                .price(6000)
                .soldCnt(0)
                .stockCnt(20)
                .info("짜장면입니다")
                .build();
    }
}
