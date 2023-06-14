package com.solo.delivery.order.mapper;

import com.solo.delivery.order.dto.OrderDetailResponseDto;
import com.solo.delivery.order.dto.OrderPostDto;
import com.solo.delivery.order.dto.OrderResponseDto;
import com.solo.delivery.order.entity.Order;
import com.solo.delivery.order.entity.OrderDetail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    public Order orderPostDtoToOrder(OrderPostDto orderPostDto) {
        return Order.builder()
                .address(orderPostDto.getAddress())
                .phone(orderPostDto.getPhone())
                .name(orderPostDto.getName())
                .requirement(orderPostDto.getRequirement())
                .build();
    }


    public OrderResponseDto orderToOrderResponseDto(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .memberId(order.getMember().getMemberId())
                .address(order.getAddress())
                .phone(order.getPhone())
                .name(order.getName())
                .requirement(order.getRequirement())
                .orderStatus(order.getOrderStatus().getStepDescription())
                .createdAt(order.getCreatedAt())
                .orderDetails(orderDetailsToOrderDetailResponseDtos(order.getOrderDetails()))
                .orderPrice(order.getOrderPrice())
                .build();
    }

    public List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders) {
        return orders
                .stream()
                .map(order -> OrderResponseDto.builder()
                        .orderId(order.getOrderId())
                        .memberId(order.getMember().getMemberId())
                        .address(order.getAddress())
                        .phone(order.getPhone())
                        .name(order.getName())
                        .requirement(order.getRequirement())
                        .orderStatus(order.getOrderStatus().getStepDescription())
                        .createdAt(order.getCreatedAt())
                        .orderDetails(orderDetailsToOrderDetailResponseDtos(order.getOrderDetails()))
                        .orderPrice(order.getOrderPrice())
                        .build())
                .collect(Collectors.toList());
    }

    public List<OrderDetailResponseDto> orderDetailsToOrderDetailResponseDtos(List<OrderDetail> orderDetails) {
        return orderDetails
                .stream()
                .map(orderDetail -> OrderDetailResponseDto.builder()
                        .orderDetailId(orderDetail.getOrderDetailId())
                        .itemOrderCnt(orderDetail.getItemOrderCnt())
                        .itemId(orderDetail.getItemId())
                        .itemName(orderDetail.getItemName())
                        .itemPrice(orderDetail.getItemPrice())
                        .build())
                .collect(Collectors.toList());
    }
}
