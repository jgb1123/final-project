package com.solo.delivery.cart.mapper;

import com.solo.delivery.cart.dto.CartPatchDto;
import com.solo.delivery.cart.dto.CartPostDto;
import com.solo.delivery.cart.dto.CartResponseDto;
import com.solo.delivery.cart.entity.Cart;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {
    public Cart cartPostDtoToCart(CartPostDto cartPostDto) {
        return Cart.builder()
                .itemCnt(cartPostDto.getItemCnt())
                .build();
    }

    public Cart cartPatchDtoToCart(CartPatchDto cartPatchDto) {
        return Cart.builder()
                .itemCnt(cartPatchDto.getItemCnt())
                .build();
    }

    public CartResponseDto cartToCartResponseDto(Cart cart) {
        return CartResponseDto.builder()
                .cartId(cart.getCartId())
                .itemCnt(cart.getItemCnt())
                .itemId(cart.getItem().getItemId())
                .itemPrice(cart.getItem().getPrice())
                .itemName(cart.getItem().getItemName())
                .build();
    }

    public List<CartResponseDto> cartsToCartResponseDtos(List<Cart> carts) {
        return carts
                .stream()
                .map(cart -> cartToCartResponseDto(cart))
                .collect(Collectors.toList());
    }
}
