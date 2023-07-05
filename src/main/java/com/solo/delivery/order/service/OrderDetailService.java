package com.solo.delivery.order.service;

import com.solo.delivery.item.entity.Item;
import com.solo.delivery.order.dto.OrderDetailPostDto;
import com.solo.delivery.order.entity.OrderDetail;
import com.solo.delivery.order.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    public OrderDetail createOrderDetail(OrderDetailPostDto orderDetailPostDto, Item item) {
        return OrderDetail.builder()
                .itemOrderCnt(orderDetailPostDto.getItemOrderCnt())
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .itemPrice(item.getPrice())
                .build();
    }

    public OrderDetail saveOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }
}
