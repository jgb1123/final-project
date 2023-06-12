package com.solo.delivery.order.repository;

import com.solo.delivery.member.entity.Member;
import com.solo.delivery.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByMember(Member member, Pageable pageable);
}
