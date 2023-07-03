package com.solo.delivery.cart;

import com.solo.delivery.cart.entity.Cart;
import com.solo.delivery.cart.repository.CartRepository;
import com.solo.delivery.dummy.CartDummy;
import com.solo.delivery.dummy.MemberDummy;
import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.exception.ExceptionCode;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.repository.MemberRepository;
import com.solo.delivery.querydsl.config.QuerydslConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
public class CartRepositoryTest {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void saveTest() {
        Cart cart = CartDummy.createCart1();

        Cart savedCart = cartRepository.save(cart);

        assertThat(savedCart.getItemCnt()).isEqualTo(cart.getItemCnt());
    }

    @Test
    void findAllByMemberTest() {
        Cart cart1 = CartDummy.createCart1();
        Cart cart2 = CartDummy.createCart2();
        Member member = MemberDummy.createMember1();
        Member savedMember = memberRepository.save(member);
        cart1.changeMember(member);
        cart2.changeMember(member);
        Cart savedCart1 = cartRepository.save(cart1);
        Cart savedCart2 = cartRepository.save(cart2);

        Page<Cart> cartPage = cartRepository.findAllByMember(savedMember, PageRequest.of(0, 10, Sort.by("cartId").ascending()));
        List<Cart> carts = cartPage.getContent();

        assertThat(carts).contains(savedCart1);
        assertThat(carts).contains(savedCart2);
    }

    @Test
    void updateTest() {
        Cart modifiedCart = CartDummy.createCart2();
        Cart cart = CartDummy.createCart1();
        Cart savedCart = cartRepository.save(cart);
        Cart foundCart = cartRepository.findById(savedCart.getCartId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CART_NOT_FOUND));

        foundCart.changeCartContent(modifiedCart);
        Cart updatedCart = cartRepository.findById(savedCart.getCartId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CART_NOT_FOUND));

        assertThat(updatedCart.getItemCnt()).isEqualTo(modifiedCart.getItemCnt());
    }

    @Test
    void deleteTest() {
        Cart cart = CartDummy.createCart1();
        Cart savedCart = cartRepository.save(cart);
        Cart foundCart = cartRepository.findById(savedCart.getCartId()).orElse(null);

        cartRepository.delete(savedCart);
        Cart foundCartAfterDelete = cartRepository.findById(savedCart.getCartId()).orElse(null);

        assertThat(foundCart).isNotNull();
        assertThat(foundCartAfterDelete).isNull();;
    }

    @Test
    void resetTest() {
        Member member = MemberDummy.createMember1();
        Member savedMember = memberRepository.save(member);
        Cart cart1 = CartDummy.createCart1();
        Cart cart2 = CartDummy.createCart2();
        cart1.changeMember(savedMember);
        cart2.changeMember(savedMember);
        cartRepository.save(cart1);
        cartRepository.save(cart2);

        long cntBeforeReset = cartRepository.findAllByMember(savedMember, PageRequest.of(0, 10, Sort.by("cartId").ascending())).getTotalElements();
        cartRepository.deleteByMember(savedMember);
        long cntAfterReset = cartRepository.findAllByMember(savedMember, PageRequest.of(0, 10, Sort.by("cartId").ascending())).getTotalElements();

        assertThat(cntBeforeReset- cntAfterReset).isEqualTo(2);

    }
}
