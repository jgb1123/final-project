package com.solo.delivery.dummy;

import com.solo.delivery.order.dto.OrderDetailPostDto;
import com.solo.delivery.order.dto.OrderDetailResponseDto;

public interface OrderDetailDummy {
    static OrderDetailPostDto createPostDto1() {
        return OrderDetailPostDto.builder()
                .itemId(1L)
                .itemOrderCnt(1)
                .build();
    }

    static OrderDetailPostDto createPostDto2() {
        return OrderDetailPostDto.builder()
                .itemId(2L)
                .itemOrderCnt(2)
                .build();
    }

    static OrderDetailResponseDto createResponseDto1() {
        return OrderDetailResponseDto.builder()
                .orderDetailId(1L)
                .itemOrderCnt(1)
                .itemId(1L)
                .itemName("김치볶음밥")
                .itemPrice(8000)
                .build();
    }

    static OrderDetailResponseDto createResponseDto2() {
        return OrderDetailResponseDto.builder()
                .orderDetailId(2L)
                .itemOrderCnt(2)
                .itemId(2L)
                .itemName("짜장면")
                .itemPrice(6000)
                .build();
    }

    static OrderDetailResponseDto createResponseDto3() {
        return OrderDetailResponseDto.builder()
                .orderDetailId(3L)
                .itemOrderCnt(3)
                .itemId(1L)
                .itemName("김치볶음밥")
                .itemPrice(8000)
                .build();
    }

    static OrderDetailResponseDto createResponseDto4() {
        return OrderDetailResponseDto.builder()
                .orderDetailId(4L)
                .itemOrderCnt(4)
                .itemId(2L)
                .itemName("짜장면")
                .itemPrice(6000)
                .build();
    }
}
