package com.solo.delivery.order;

import com.solo.delivery.dummy.*;
import com.solo.delivery.item.entity.Item;
import com.solo.delivery.item.service.ItemService;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.order.dto.OrderDetailPostDto;
import com.solo.delivery.order.dto.OrderPostDto;
import com.solo.delivery.order.entity.Order;
import com.solo.delivery.order.repository.OrderDetailRepository;
import com.solo.delivery.order.repository.OrderRepository;
import com.solo.delivery.order.service.OrderService;
import com.solo.delivery.store.entity.Store;
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
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private ItemService itemService;

    @Test
    void createOrderTest() {
        Order order = OrderDummy.createOrder1();
        Member member = MemberDummy.createMember1();
        Item item = ItemDummy.createItem1();
        Store store = StoreDummy.createStore1();
        item.changeStore(store);
        OrderPostDto orderPostDto = OrderDummy.createPostDto();
        List<OrderDetailPostDto> orderDetailPostDtos = orderPostDto.getOrderDetails();
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member);
        given(itemService.findVerifiedItem(Mockito.anyLong()))
                .willReturn(item);
        given(orderRepository.save(Mockito.any(Order.class)))
                .willReturn(order);

        Order savedOrder = orderService.createOrder(order, orderDetailPostDtos, "hgd@gmail.com");

        assertThat(savedOrder.getAddress()).isEqualTo(order.getAddress());
        assertThat(savedOrder.getRequirement()).isEqualTo(order.getRequirement());
        assertThat(savedOrder.getStoreId()).isEqualTo(item.getStore().getStoreId());
    }

    @Test
    void findOrderTest() {
        Order order = OrderDummy.createOrder1();
        given(orderRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(order));

        Order foundOrder = orderService.findOrder(1L);

        assertThat(foundOrder.getOrderPrice()).isEqualTo(order.getOrderPrice());
        assertThat(foundOrder.getName()).isEqualTo(order.getName());
        assertThat(foundOrder.getCreatedAt()).isEqualTo(order.getCreatedAt());
    }

    @Test
    void findOrdersTest() {
        Order order1 = OrderDummy.createOrder1();
        Order order2 = OrderDummy.createOrder2();
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(new Member());
        given(orderRepository.findAllByMember(Mockito.any(Member.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(order1, order2), PageRequest.of(0, 10, Sort.by("orderId").ascending()), 2));

        Page<Order> orderPage = orderService.findOrders("hgd@gmail.com", 1, 10);
        List<Order> orders = orderPage.getContent();

        assertThat(orders).contains(order1);
        assertThat(orders).contains(order2);
        assertThat(orderPage.getTotalElements()).isEqualTo(2);
    }

    @Test
    void updateOrderTest() {
        Order order = OrderDummy.createOrder1();
        given(orderRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(order));

        Order updatedOrder = orderService.updateOrder(1L, "주문 확정");

        assertThat(updatedOrder.getOrderStatus().getStepDescription()).isEqualTo("주문 확정");
    }
    
    @Test
    void cancelOrderTest() {
        Order order = OrderDummy.createOrder1();
        given(orderRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(order));

        Order canceledOrder = orderService.cancelOrder(1L);

        assertThat(canceledOrder.getOrderStatus().getStepDescription()).isEqualTo("주문 취소");
    }
}
