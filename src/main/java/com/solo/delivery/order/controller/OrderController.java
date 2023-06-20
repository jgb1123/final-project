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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity postOrder(@Valid @RequestBody OrderPostDto orderPostDto,
                                    @AuthenticationPrincipal String email) {
        Order order = orderMapper.orderPostDtoToOrder(orderPostDto);
        List<OrderDetailPostDto> orderDetailPostDtos = orderPostDto.getOrderDetails();
        orderService.createOrder(order, orderDetailPostDtos, email);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity getOrder(@Positive @PathVariable Long orderId) {
        Order foundOrder = orderService.findOrder(orderId);
        OrderResponseDto orderResponseDto = orderMapper.orderToOrderResponseDto(foundOrder);
        return new ResponseEntity<>(new SingleResponseDto<>(orderResponseDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getOrders(@Positive @RequestParam(required = false, defaultValue = "1") int page,
                                    @Positive @RequestParam(required = false, defaultValue = "10") int size,
                                    @AuthenticationPrincipal String email) {
        Page<Order> orderPage = orderService.findOrders(email, page, size);
        List<Order> orders = orderPage.getContent();
        List<OrderResponseDto> orderResponseDtos = orderMapper.ordersToOrderResponseDtos(orders);
        return new ResponseEntity<>(new MultiResponseDto<>(orderResponseDtos, orderPage), HttpStatus.OK);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity patchOrder(@Positive @PathVariable Long orderId,
                                     @Valid @RequestBody OrderPatchDto orderPatchDto) {
        String orderStatus = orderPatchDto.getOrderStatus();
        orderService.updateOrder(orderId, orderStatus);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity deleteOrder(@Positive @PathVariable Long orderId,
                                      @AuthenticationPrincipal String email) {
        orderService.cancelOrder(orderId, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
