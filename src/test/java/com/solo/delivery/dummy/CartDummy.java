package com.solo.delivery.dummy;

import com.solo.delivery.cart.dto.CartPatchDto;
import com.solo.delivery.cart.dto.CartPostDto;
import com.solo.delivery.cart.dto.CartResponseDto;
import com.solo.delivery.cart.entity.Cart;

public interface CartDummy {
    static Cart createCart1() {
        return Cart.builder()
                .cartId(1L)
                .itemCnt(3)
                .build();
    }

    static Cart createCart2() {
        return Cart.builder()
                .cartId(2L)
                .itemCnt(5)
                .build();
    }

    static CartPostDto createPostDto() {
        return CartPostDto.builder()
                .itemCnt(3)
                .build();
    }

    static CartPatchDto createPatchDto() {
        return CartPatchDto.builder()
                .itemCnt(2)
                .build();
    }

    static CartResponseDto createResponseDto1() {
        return CartResponseDto.builder()
                .cartId(1L)
                .itemCnt(3)
                .itemId(1L)
                .itemName("김치볶음밥")
                .itemPrice(8000)
                .build();
    }

    static CartResponseDto createResponseDto2() {
        return CartResponseDto.builder()
                .cartId(2L)
                .itemCnt(2)
                .itemId(2L)
                .itemName("짜장면")
                .itemPrice(6000)
                .build();
    }
}
