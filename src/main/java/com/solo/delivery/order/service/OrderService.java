package com.solo.delivery.order.service;

import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.exception.ExceptionCode;
import com.solo.delivery.item.entity.Item;
import com.solo.delivery.item.service.ItemService;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.order.dto.OrderDetailPostDto;
import com.solo.delivery.order.entity.Order;
import com.solo.delivery.order.entity.OrderDetail;
import com.solo.delivery.order.repository.OrderDetailRepository;
import com.solo.delivery.order.repository.OrderRepository;
import com.solo.delivery.store.entity.Store;
import com.solo.delivery.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final StoreService storeService;
    private final ItemService itemService;
    private final MemberService memberService;

    public Order createOrder(Order order, List<OrderDetailPostDto> orderDetailPostDtos, String email) {
        Member foundMember = memberService.findVerifiedMember(email);
        order.changeMember(foundMember);
        order.changeOrderInfo(foundMember);
        int orderPrice = 0;
        for(OrderDetailPostDto orderDetailPostDto : orderDetailPostDtos) {
            Item item = itemService.findVerifiedItem(orderDetailPostDto.getItemId());
            itemService.checkStockCnt(orderDetailPostDto.getItemOrderCnt(), item);
            OrderDetail orderDetail = OrderDetail.builder()
                    .itemOrderCnt(orderDetailPostDto.getItemOrderCnt())
                    .itemId(item.getItemId())
                    .itemName(item.getItemName())
                    .itemPrice(item.getPrice())
                    .build();
            orderDetail.changeOrder(order);
            orderDetailRepository.save(orderDetail);
            if(order.getStoreId() == null) {
                order.changeStoreId(item.getStore().getStoreId());
            }
            orderPrice += orderDetail.getItemOrderCnt() * orderDetail.getItemPrice();
        }
        order.changeOrderPrice(orderPrice);
        order.changeOrderStatus(Order.OrderStatus.ORDER_REQUEST);
        return orderRepository.save(order);
    }

    public Order findOrder(Long orderId) {
        return findVerifiedOrder(orderId);
    }

    public Page<Order> findOrders(String email, Pageable pageable) {
        Member foundMember = memberService.findVerifiedMember(email);
        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        return orderRepository.findAllByMember(foundMember, pageable);
    }

    public Order updateOrder(Long orderId, String orderStatus) {
        Order foundOrder = findVerifiedOrder(orderId);
        if(foundOrder.getOrderStatus().getStepDescription().equals("주문 완료")) {
            throw new BusinessLogicException(ExceptionCode.ORDER_CANNOT_CHANGE);
        }
        foundOrder.changeOrderStatus(Order.OrderStatus.of(orderStatus));
        if(orderStatus.equals("주문 완료")) {
            Store foundStore = storeService.findVerifiedStore(foundOrder.getStoreId());
            foundStore.increaseOrderCnt();
        }
        return foundOrder;
    }

    public Order cancelOrder(Long orderId, String email) {
        Member foundMember = memberService.findVerifiedMember(email);
        Order foundOrder = findVerifiedOrder(orderId);
        if(foundOrder.getMember().getMemberId() != foundMember.getMemberId()) {
            throw new BusinessLogicException(ExceptionCode.ORDER_CANNOT_CHANGE);
        }
        int step = foundOrder.getOrderStatus().getStepNumber();
        if(step > 0) {
            throw new BusinessLogicException(ExceptionCode.ORDER_CANNOT_CHANGE);
        }
        foundOrder.changeOrderStatus(Order.OrderStatus.ORDER_CANCEL);
        return foundOrder;
    }

    public Order findVerifiedOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
    }
}
