package com.solo.delivery.item.repository;

import com.solo.delivery.item.entity.Item;
import com.solo.delivery.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByStore(Store store, Pageable pageable);
}
