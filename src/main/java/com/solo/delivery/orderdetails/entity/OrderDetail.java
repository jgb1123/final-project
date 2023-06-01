package com.solo.delivery.orderdetails.entity;

import com.solo.delivery.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderItemId;

    @Column
    private int orderItemCnt;

    @Column
    private long itemId;

    @Column
    private String itemName;

    @Column
    private int itemPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    public void changeOrder(Order order) {
        if(this.order != null) {
            this.order.getOrderDetails().remove(this);
        }
        this.order = order;
        if(!order.getOrderDetails().contains(this)) {
            order.addOrderItem(this);
        }
    }
}
