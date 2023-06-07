package com.solo.delivery.store.repository;

import com.solo.delivery.store.entity.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory, String> {
}
