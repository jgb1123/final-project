package com.solo.delivery.order.entity;

import com.solo.delivery.basetime.BaseTimeEntity;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.orderdetails.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ORDERS")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Column
    private long storeId;

    @Column
    private String address;

    @Column
    private String Phone;

    @Column
    private String name;

    @Column
    private String requirement;

    @Column
    private int orderPrice;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @BatchSize(size = 1000)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public void changeMember(Member member) {
        if(this.member != null) {
            this.member.getOrders().remove(this);
        }
        this.member = member;
        if(!member.getOrders().contains(this)) {
            member.addOrder(this);
        }
    }

    public void addOrderItem(OrderDetail orderItem) {
        this.orderDetails.add(orderItem);
        if(orderItem.getOrder() != this) {
            orderItem.changeOrder(this);
        }
    }
}
