package com.solo.delivery.store.repository;

import com.solo.delivery.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {
    Page<Store> findAll(Pageable pageable);
    Page<Store> findAllByStoreCategoryStoreCategoryIdStartingWith(String categoryId, Pageable pageable);
}
