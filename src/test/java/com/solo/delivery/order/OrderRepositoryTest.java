package com.solo.delivery.order;

import com.solo.delivery.dummy.MemberDummy;
import com.solo.delivery.dummy.OrderDummy;
import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.exception.ExceptionCode;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.repository.MemberRepository;
import com.solo.delivery.order.entity.Order;
import com.solo.delivery.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void saveTest() {
        Order order = OrderDummy.createOrder1();

        Order savedOrder = orderRepository.save(order);

        assertThat(savedOrder.getAddress()).isEqualTo(order.getAddress());
        assertThat(savedOrder.getRequirement()).isEqualTo(order.getRequirement());
        assertThat(savedOrder.getOrderPrice()).isEqualTo(order.getOrderPrice());
    }

    @Test
    void findTest() {
        Order order = OrderDummy.createOrder1();
        Order savedOrder = orderRepository.save(order);

        Order foundOrder = orderRepository.findById(savedOrder.getOrderId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));

        assertThat(foundOrder.getOrderPrice()).isEqualTo(savedOrder.getOrderPrice());
        assertThat(foundOrder.getRequirement()).isEqualTo(savedOrder.getRequirement());
        assertThat(foundOrder.getModifiedAt()).isEqualTo(savedOrder.getCreatedAt());
    }

    @Test
    void findAllByMemberTest() {
        Order order1 = OrderDummy.createOrder1();
        Order order2 = OrderDummy.createOrder2();
        Member member = MemberDummy.createMember1();
        Member savedMember = memberRepository.save(member);
        order1.changeMember(savedMember);
        order2.changeMember(savedMember);
        Order savedOrder1 = orderRepository.save(order1);
        Order savedOrder2 = orderRepository.save(order2);

        Page<Order> orderPage = orderRepository.findAllByMember(savedMember, PageRequest.of(0, 10, Sort.by("orderId").ascending()));
        List<Order> orders = orderPage.getContent();

        assertThat(orders).contains(savedOrder1);
        assertThat(orders).contains(savedOrder2);
    }

    @Test
    void updateTest() {
        Order order = OrderDummy.createOrder1();
        Order savedOrder = orderRepository.save(order);
        Order foundOrder = orderRepository.findById(savedOrder.getOrderId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));

        foundOrder.changeOrderStatus(Order.OrderStatus.ORDER_CANCEL);
        Order updatedOrder = orderRepository.findById(savedOrder.getOrderId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));

        assertThat(updatedOrder.getOrderStatus().getStepDescription()).isEqualTo("주문 취소");
    }
}
