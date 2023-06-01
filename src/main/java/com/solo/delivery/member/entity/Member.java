package com.solo.delivery.member.entity;

import com.solo.delivery.order.entity.Order;
import com.solo.delivery.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @Column
    private String memberName;

    @Column
    private String nickName;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String address;

    @Column
    private String phone;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Review> reviews = new ArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
        if(order.getMember() != this) {
            order.changeMember(this);
        };
    }

    public void addReview(Review review) {
        this.reviews.add(review);
        if(review.getMember() != this){
            review.changeMember(this);
        }
    }
}
