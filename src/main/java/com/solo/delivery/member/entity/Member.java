package com.solo.delivery.member.entity;

import com.solo.delivery.cart.entity.Cart;
import com.solo.delivery.order.entity.Order;
import com.solo.delivery.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @Column
    private String name;

    @Column
    private String nickname;

    @Column
    private String email;

    @Column
    private String address;

    @Column
    private String phone;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Review> reviews = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Cart> carts = new ArrayList<>();

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

    public void addCart(Cart cart){
        carts.add(cart);
        if(cart.getMember() != this){
            cart.changeMember(this);
        }
    }

    public void changeMemberContent(Member member) {
        if(member.getPhone() != null) this.phone = member.getPhone();
        if(member.getNickname() != null) this.nickname = member.getNickname();
        if(member.getAddress() != null) this.address = member.getAddress();
    }

    public void changeSignUpInfo(String email, String memberName) {
        this.email = email;
        this.name = memberName;
    }

    public void changeRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
