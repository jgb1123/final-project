package com.solo.delivery.order;

import com.solo.delivery.dummy.*;
import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.item.entity.Item;
import com.solo.delivery.item.service.ItemService;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.order.dto.OrderDetailPostDto;
import com.solo.delivery.order.dto.OrderPostDto;
import com.solo.delivery.order.entity.Order;
import com.solo.delivery.order.entity.OrderDetail;
import com.solo.delivery.order.repository.OrderDetailRepository;
import com.solo.delivery.order.repository.OrderRepository;
import com.solo.delivery.order.service.OrderDetailService;
import com.solo.delivery.order.service.OrderService;
import com.solo.delivery.store.entity.Store;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
    private OrderDetailService orderDetailService;

    @Mock
    private MemberService memberService;

    @Mock
    private ItemService itemService;

    @Test
    @DisplayName("OrderService createOrder")
    void createOrderTest() {
        Order order = OrderDummy.createOrder1();
        Member member = MemberDummy.createMember1();
        Item item1 = ItemDummy.createItem1();
        Item item2 = ItemDummy.createItem2();
        Store store = StoreDummy.createStore1();
        item1.changeStore(store);
        item2.changeStore(store);
        OrderPostDto orderPostDto = OrderDummy.createPostDto();
        OrderDetail orderDetail1 = OrderDetailDummy.createOrderDetail1();
        OrderDetail orderDetail2 = OrderDetailDummy.createOrderDetail2();
        List<OrderDetailPostDto> orderDetailPostDtos = orderPostDto.getOrderDetails();
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member);
        given(itemService.findVerifiedItem(Mockito.anyLong()))
                .willReturn(item1)
                .willReturn(item2);
        given(orderDetailService.createOrderDetail(Mockito.any(OrderDetailPostDto.class), Mockito.any(Item.class)))
                .willReturn(orderDetail1)
                .willReturn(orderDetail2);
        given(orderRepository.save(Mockito.any(Order.class)))
                .willReturn(order);

        Order savedOrder = orderService.createOrder(order, orderDetailPostDtos, "hgd@gmail.com");

        assertThat(savedOrder.getAddress()).isEqualTo(order.getAddress());
        assertThat(savedOrder.getRequirement()).isEqualTo(order.getRequirement());
        assertThat(savedOrder.getStoreId()).isEqualTo(item1.getStore().getStoreId());
    }

    @Test
    @DisplayName("OrderService createOrder 같은 상점의 상품으로만 생성 가능")
    void createOrderItemsOtherStoreTest() {
        Order order = OrderDummy.createOrder1();
        Member member = MemberDummy.createMember1();
        Item item1 = ItemDummy.createItem1();
        Store store1 = StoreDummy.createStore1();
        item1.changeStore(store1);
        Item item2 = ItemDummy.createItem2();
        Store store2 = StoreDummy.createStore2();
        item2.changeStore(store2);
        OrderPostDto orderPostDto = OrderDummy.createPostDto();
        List<OrderDetailPostDto> orderDetailPostDtos = orderPostDto.getOrderDetails();
        OrderDetail orderDetail1 = OrderDetailDummy.createOrderDetail1();
        OrderDetail orderDetail2 = OrderDetailDummy.createOrderDetail2();
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member);
        given(itemService.findVerifiedItem(Mockito.anyLong()))
                .willReturn(item1)
                .willReturn(item2);
        given(orderDetailService.createOrderDetail(Mockito.any(OrderDetailPostDto.class), Mockito.any(Item.class)))
                .willReturn(orderDetail1)
                .willReturn(orderDetail2);

        Assertions.assertThrows(BusinessLogicException.class, () -> orderService.createOrder(order, orderDetailPostDtos, "hgd@gmail.com"));
    }

    @Test
    @DisplayName("OrderService findOrder")
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
    @DisplayName("OrderService findOrders")
    void findOrdersTest() {
        Order order1 = OrderDummy.createOrder1();
        Order order2 = OrderDummy.createOrder2();
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(new Member());
        given(orderRepository.findAllByMember(Mockito.any(Member.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(order1, order2), PageRequest.of(0, 10, Sort.by("orderId").ascending()), 2));

        Page<Order> orderPage = orderService.findOrders("hgd@gmail.com", PageRequest.of(1, 10, Sort.by("orderId").descending()));
        List<Order> orders = orderPage.getContent();

        assertThat(orders).contains(order1);
        assertThat(orders).contains(order2);
        assertThat(orderPage.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("OrderService updateOrder")
    void updateOrderTest() {
        Order order = OrderDummy.createOrder1();
        given(orderRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(order));

        Order updatedOrder = orderService.updateOrder(1L, "주문 확정");

        assertThat(updatedOrder.getOrderStatus().getStepDescription()).isEqualTo("주문 확정");
    }
    
    @Test
    @DisplayName("OrderService cancelOrder")
    void cancelOrderTest() {
        Order order = OrderDummy.createOrder1();
        Member member = MemberDummy.createMember1();
        order.changeMember(member);
        given(orderRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(order));
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member);

        Order canceledOrder = orderService.cancelOrder(1L, "hgd@gmail.com");

        assertThat(canceledOrder.getOrderStatus().getStepDescription()).isEqualTo("주문 취소");
    }

    @Test
    @DisplayName("OrderService cancelOrder 주문요청상태 외에는 취소 불가")
    void cancelOrderOnlyRequestStatusTest() {
        Order order = OrderDummy.createOrder1();
        order.changeOrderStatus(Order.OrderStatus.ORDER_COMPLETE);
        Member member = MemberDummy.createMember1();
        order.changeMember(member);
        given(orderRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(order));
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member);

        Assertions.assertThrows(BusinessLogicException.class, () -> orderService.cancelOrder(1L, "hgd@gmail.com"));
    }

    @Test
    @DisplayName("OrderService cancelOrder 주문한 본인만 취소 가능")
    void cancelOrderOnlySameMemberTest() {
        Order order = OrderDummy.createOrder2();
        Member member1 = MemberDummy.createMember1();
        order.changeMember(member1);
        Member member2 = MemberDummy.createMember2();
        given(orderRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(order));
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member2);

        Assertions.assertThrows(BusinessLogicException.class, () -> orderService.cancelOrder(1L, "hgd@gmail.com"));
    }
}
