package com.solo.delivery.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private Long orderId;
    private Long memberId;
    private String address;
    private String phone;
    private String name;
    private String requirement;
    private Integer orderPrice;
    private String orderStatus;
    private LocalDateTime createdAt;
    private List<OrderDetailResponseDto> orderDetails;
}
