package com.solo.delivery.cart.entity;

import com.solo.delivery.item.entity.Item;
import com.solo.delivery.member.entity.Member;
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
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @Column
    private Integer itemCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    public void changeMember(Member member) {
        if(this.member != null) {
            this.member.getCarts().remove(this);
        }
        this.member = member;
        if(!member.getCarts().contains(this)) {
            member.addCart(this);
        }
    }

    public void changeItem(Item item) {
        this.item = item;
    }

    public void changeCartContent(Cart cart) {
        if(cart.getItemCnt() != null) this.itemCnt = cart.getItemCnt();
    }
}
