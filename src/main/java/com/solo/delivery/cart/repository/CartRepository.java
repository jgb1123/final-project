package com.solo.delivery.cart.repository;

import com.solo.delivery.cart.entity.Cart;
import com.solo.delivery.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @EntityGraph(attributePaths = "item")
    Page<Cart> findAllByMember(Member member, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.member = :member")
    void deleteByMember(Member member);
}
