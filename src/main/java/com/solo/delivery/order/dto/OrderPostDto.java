package com.solo.delivery.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPostDto {
    private List<OrderDetailPostDto> orderDetails;
    private String address;
    private String phone;
    private String name;
    private String requirement;
}
