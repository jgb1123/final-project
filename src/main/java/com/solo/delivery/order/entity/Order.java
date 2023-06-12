package com.solo.delivery.order.entity;

import com.solo.delivery.basetime.BaseTimeEntity;
import com.solo.delivery.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity(name = "ORDERS")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column
    private Long storeId;

    @Column
    private String address;

    @Column
    private String phone;

    @Column
    private String name;

    @Column
    private String requirement;

    @Column
    private Integer orderPrice;

    @Column
    private OrderStatus orderStatus = OrderStatus.ORDER_REQUEST;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @Builder.Default
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

    public enum OrderStatus {
        ORDER_REQUEST(0, "주문 요청"),
        ORDER_CONFIRM(1, "주문 확정"),
        ORDER_COMPLETE(2, "주문 완료"),
        ORDER_CANCEL(3, "주문 취소");

        private static final Map<String, String> map = Collections.unmodifiableMap(
                Stream.of(values()).collect(Collectors.toMap(OrderStatus::getStepDescription, OrderStatus::name))
        );

        @Getter
        private final int stepNumber;

        @Getter
        private final String stepDescription;

        OrderStatus(int stepNumber, String stepDescription) {
            this.stepNumber = stepNumber;
            this.stepDescription = stepDescription;
        }
        public static OrderStatus of(String stepDescription){
            return OrderStatus.valueOf(map.get(stepDescription));
        }
    }

    public void changeOrderInfo(Member member) {
        if(this.address == null) this.address = member.getAddress();
        if(this.phone == null) this.phone = member.getPhone();
        if(this.name == null) this.name = member.getName();
        if(this.requirement == null) this.requirement = "조심히 와주세요";
    }

    public void changeOrderPrice(Integer orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void changeOrderStatus(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

    public void changeStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
