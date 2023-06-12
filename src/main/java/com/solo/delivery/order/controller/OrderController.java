package com.solo.delivery.order.controller;

import com.solo.delivery.dto.MultiResponseDto;
import com.solo.delivery.dto.SingleResponseDto;
import com.solo.delivery.order.dto.OrderDetailPostDto;
import com.solo.delivery.order.dto.OrderPatchDto;
import com.solo.delivery.order.dto.OrderPostDto;
import com.solo.delivery.order.dto.OrderResponseDto;
import com.solo.delivery.order.entity.Order;
import com.solo.delivery.order.mapper.OrderMapper;
import com.solo.delivery.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity postOrder(@RequestBody OrderPostDto orderPostDto,
                                    @AuthenticationPrincipal String email) {
        Order order = orderMapper.orderPostDtoToOrder(orderPostDto);
        List<OrderDetailPostDto> orderDetailPostDtos = orderPostDto.getOrderDetails();
        orderService.createOrder(order, orderDetailPostDtos, email);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity getOrder(@PathVariable Long orderId) {
        Order foundOrder = orderService.findOrder(orderId);
        OrderResponseDto orderResponseDto = orderMapper.orderToOrderResponseDto(foundOrder);
        return new ResponseEntity<>(new SingleResponseDto<>(orderResponseDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getOrders(int page,
                                    int size,
                                    @AuthenticationPrincipal String email) {
        Page<Order> orderPage = orderService.findOrders(email, page, size);
        List<Order> orders = orderPage.getContent();
        List<OrderResponseDto> orderResponseDtos = orderMapper.ordersToOrderResponseDtos(orders);
        return new ResponseEntity<>(new MultiResponseDto<>(orderResponseDtos, orderPage), HttpStatus.OK);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity patchOrder(@PathVariable Long orderId,
                                     @RequestBody OrderPatchDto orderPatchDto) {
        String orderStatus = orderPatchDto.getOrderStatus();
        orderService.updateOrder(orderId, orderStatus);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity deleteOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
