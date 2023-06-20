package com.solo.delivery.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailPostDto {
    @NotNull
    @Positive
    private Long itemId;

    @NotNull
    @Positive
    private Integer itemOrderCnt;
}
