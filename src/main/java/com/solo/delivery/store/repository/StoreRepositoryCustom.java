package com.solo.delivery.store.repository;

import com.solo.delivery.store.dto.StoreResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreRepositoryCustom {
    Page<StoreResponseDto> searchStore(String word, Integer minimumOrderPrice, Integer deliveryFee, Pageable pageable);
}
