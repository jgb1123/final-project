package com.solo.delivery.dummy;

import com.solo.delivery.order.dto.OrderPatchDto;
import com.solo.delivery.order.dto.OrderPostDto;
import com.solo.delivery.order.dto.OrderResponseDto;
import com.solo.delivery.order.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderDummy {
    static Order createOrder1() {
        return Order.builder()
                .orderId(1L)
                .storeId(1L)
                .address("서울시 구로구 고척동 111-11 11호")
                .phone("010-1234-5678")
                .name("홍길동")
                .requirement("문앞이요")
                .orderPrice(20000)
                .orderStatus(Order.OrderStatus.ORDER_REQUEST)
                .build();
    }

    static Order createOrder2() {
        return Order.builder()
                .orderId(2L)
                .storeId(1L)
                .address("서울시 구로구 고척동 222-22 22호")
                .phone("010-8765-4321")
                .name("이순신")
                .requirement("조심히 와주세요")
                .orderPrice(48000)
                .orderStatus(Order.OrderStatus.ORDER_REQUEST)
                .build();
    }

    static OrderPostDto createPostDto() {
        return OrderPostDto.builder()
                .orderDetails(List.of(OrderDetailDummy.createPostDto1(), OrderDetailDummy.createPostDto2()))
                .address("서울시 구로구 고척동 111-11 11호")
                .phone("010-1234-5678")
                .name("홍길동")
                .requirement("문앞이요")
                .build();
    }

    static OrderPatchDto createPatchDto() {
        return OrderPatchDto.builder()
                .orderStatus("주문 확정")
                .build();
    }

    static OrderResponseDto createResponseDto1() {
        return OrderResponseDto.builder()
                .orderId(1L)
                .memberId(1L)
                .address("서울시 구로구 고척동 111-11 11호")
                .phone("010-1234-5678")
                .name("홍길동")
                .requirement("문앞이요")
                .orderPrice(20000)
                .orderStatus("주문 요청")
                .createdAt(LocalDateTime.of(2023, 6, 14, 0, 0, 0))
                .orderDetails(List.of(OrderDetailDummy.createResponseDto1(), OrderDetailDummy.createResponseDto2()))
                .build();
    }

    static OrderResponseDto createResponseDto2() {
        return OrderResponseDto.builder()
                .orderId(2L)
                .memberId(1L)
                .address("서울시 구로구 고척동 222-22 22호")
                .phone("010-8765-4321")
                .name("이순신")
                .requirement("안전하게 와주세요")
                .orderPrice(48000)
                .orderStatus("주문 요청")
                .createdAt(LocalDateTime.of(2023, 6, 14, 1, 0, 0))
                .orderDetails(List.of(OrderDetailDummy.createResponseDto3(), OrderDetailDummy.createResponseDto4()))
                .build();
    }
}
