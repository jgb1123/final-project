package com.solo.delivery.cart;

import com.solo.delivery.cart.entity.Cart;
import com.solo.delivery.cart.repository.CartRepository;
import com.solo.delivery.cart.service.CartService;
import com.solo.delivery.dummy.CartDummy;
import com.solo.delivery.dummy.ItemDummy;
import com.solo.delivery.dummy.MemberDummy;
import com.solo.delivery.dummy.StoreDummy;
import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.item.entity.Item;
import com.solo.delivery.item.service.ItemService;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.store.entity.Store;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private MemberService memberService;

    @Test
    void createCartTest() {
        Cart cart = CartDummy.createCart1();
        Member member = MemberDummy.createMember1();
        Item item = ItemDummy.createItem1();
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member);
        given(itemService.findVerifiedItem(Mockito.anyLong()))
                .willReturn(item);
        given(cartRepository.save(Mockito.any(Cart.class)))
                .willReturn(cart);

        Cart savedCart = cartService.createCart(cart, 1L, "hgd@gmail.com");

        assertThat(savedCart.getMember().getEmail()).isEqualTo(member.getEmail());
        assertThat(savedCart.getItem().getItemName()).isEqualTo(item.getItemName());
        assertThat(savedCart.getItemCnt()).isEqualTo(cart.getItemCnt());
    }

    @Test
    void createCartTest_itemsOtherStore() {
        Store store1 = StoreDummy.createStore1();
        Item item1 = ItemDummy.createItem1();
        item1.changeStore(store1);
        Cart cart1 = CartDummy.createCart1();
        Member member = MemberDummy.createMember1();
        cart1.changeItem(item1);
        cart1.changeMember(member);

        Store store2 = StoreDummy.createStore2();
        Item item2 = ItemDummy.createItem2();
        item2.changeStore(store2);
        Cart cart2 = CartDummy.createCart2();
        cart2.changeItem(item2);

        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member);
        given(itemService.findVerifiedItem(Mockito.anyLong()))
                .willReturn(item2);

        Assertions.assertThrows(BusinessLogicException.class, () -> cartService.createCart(cart2, 2L, "hgd@gmail.com"));
    }

    @Test
    void findCartsTest() {
        Cart cart1 = CartDummy.createCart1();
        Cart cart2 = CartDummy.createCart2();
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(new Member());
        given(cartRepository.findAllByMember(Mockito.any(Member.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(cart1, cart2), PageRequest.of(0, 10, Sort.by("cartId").descending()), 2));

        Page<Cart> cartPage = cartService.findCarts("hgd@gmail.com", PageRequest.of(1, 10, Sort.by("cartId").descending()));
        List<Cart> carts = cartPage.getContent();


        assertThat(carts).contains(cart1);
        assertThat(carts).contains(cart2);
    }

    @Test
    void updateCartTest() {
        Cart modifiedCart = CartDummy.createCart2();
        Cart cart = CartDummy.createCart1();
        given(cartRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(cart));

        Cart updatedCart = cartService.updateCart(1L, modifiedCart);

        assertThat(updatedCart.getItemCnt()).isEqualTo(modifiedCart.getItemCnt());
    }

    @Test
    void deleteCartTest() {
        Cart cart = CartDummy.createCart1();
        given(cartRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(cart));

        Assertions.assertDoesNotThrow(() -> cartService.deleteCart(1L));
    }
}
