package com.solo.delivery.cart.controller;

import com.solo.delivery.cart.dto.CartPatchDto;
import com.solo.delivery.cart.dto.CartPostDto;
import com.solo.delivery.cart.dto.CartResponseDto;
import com.solo.delivery.cart.entity.Cart;
import com.solo.delivery.cart.mapper.CartMapper;
import com.solo.delivery.cart.service.CartService;
import com.solo.delivery.dto.MultiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;
    private final CartMapper cartMapper;

    @PostMapping("/{itemId}")
    public ResponseEntity postCart(@PathVariable Long itemId,
                               @RequestBody CartPostDto cartPostDto,
                               @AuthenticationPrincipal String email) {
        Cart cart = cartMapper.cartPostDtoToCart(cartPostDto);
        Cart savedCart = cartService.createCart(cart, itemId, email);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getCarts(int page,
                                   int size,
                                   @AuthenticationPrincipal String email) {
        Page<Cart> cartPage = cartService.findCarts(email, page, size);
        List<Cart> carts = cartPage.getContent();
        List<CartResponseDto> cartResponseDtos = cartMapper.cartsToCartResponseDtos(carts);
        return new ResponseEntity<>(new MultiResponseDto<>(cartResponseDtos, cartPage), HttpStatus.OK);
    }

    @PatchMapping("/{cartId}")
    public ResponseEntity patchCart(@PathVariable Long cartId,
                                    @RequestBody CartPatchDto cartPatchDto) {
        Cart cart = cartMapper.cartPatchDtoToCart(cartPatchDto);
        Cart savedCart = cartService.updateCart(cartId, cart);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}