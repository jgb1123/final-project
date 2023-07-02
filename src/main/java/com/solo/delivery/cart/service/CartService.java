package com.solo.delivery.cart.service;

import com.solo.delivery.cart.entity.Cart;
import com.solo.delivery.cart.repository.CartRepository;
import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.exception.ExceptionCode;
import com.solo.delivery.item.entity.Item;
import com.solo.delivery.item.service.ItemService;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ItemService itemService;
    private final MemberService memberService;

    public Cart createCart(Cart cart, Long itemId, String email) {
        Member foundMember = memberService.findVerifiedMember(email);
        Item foundItem = itemService.findVerifiedItem(itemId);
        if(!foundMember.getCarts().isEmpty()) {
            Long existStoreId = foundMember.getCarts().get(0).getItem().getStore().getStoreId();
            Long currentStoreId = foundItem.getStore().getStoreId();
            if(!existStoreId.equals(currentStoreId)) {
                throw new BusinessLogicException(ExceptionCode.ONLY_ITEMS_FROM_SAME_STORE);
            }
        }
        itemService.checkStockCnt(cart.getItemCnt(), foundItem);
        cart.changeMember(foundMember);
        cart.changeItem(foundItem);
        return cartRepository.save(cart);
    }

    public Page<Cart> findCarts(String email, Pageable pageable) {
        Member foundMember = memberService.findVerifiedMember(email);
        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        return cartRepository.findAllByMember(foundMember, pageable);
    }

    public Cart updateCart(Long cartId, Cart modifiedCart) {
        Cart foundCart = findVerifiedCart(cartId);
        Item item = foundCart.getItem();
        itemService.checkStockCnt(modifiedCart.getItemCnt(), item);
        foundCart.changeCartContent(modifiedCart);
        return foundCart;
    }

    public void deleteCart(Long cartId) {
        Cart foundCart = findVerifiedCart(cartId);
        cartRepository.delete(foundCart);
    }

    public void resetCart(String email) {
        Member foundMember = memberService.findVerifiedMember(email);
        cartRepository.deleteByMember(foundMember);
    }

    public Cart findVerifiedCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CART_NOT_FOUND));
    }
}
