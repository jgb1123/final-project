package com.solo.delivery.store.service;

import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.exception.ExceptionCode;
import com.solo.delivery.store.dto.StoreResponseDto;
import com.solo.delivery.store.entity.Store;
import com.solo.delivery.store.entity.StoreCategory;
import com.solo.delivery.store.repository.StoreCategoryRepository;
import com.solo.delivery.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final StoreCategoryRepository storeCategoryRepository;

    public Store createStore(Store store, String storeCategoryId) {
        StoreCategory foundStoreCategory = findVerifiedStoreCategory(storeCategoryId);
        store.changeStoreCategory(foundStoreCategory);
        return storeRepository.save(store);
    }

    public Store findStore(Long storeId) {
        return findVerifiedStore(storeId);
    }

    public Page<Store> findStores(String categoryId, int page, int size) {
        StoreCategory foundCategory = findVerifiedStoreCategory(categoryId);
        return storeRepository.findAllByStoreCategoryStoreCategoryIdStartingWith(categoryId, PageRequest.of(page - 1, size, Sort.by("storeId").ascending()));
    }

    public Store updateStore(Long storeId, Store modifiedStore, String storeCategoryId) {
        Store foundStore = findVerifiedStore(storeId);
        if(storeCategoryId != null) {
            StoreCategory foundStoreCategory = findVerifiedStoreCategory(storeCategoryId);
            foundStore.changeStoreCategory(foundStoreCategory);
        }
        foundStore.changeStoreContent(modifiedStore);
        return foundStore;
    }

    public void deleteStore(Long storeId) {
        Store foundStore = findVerifiedStore(storeId);
        storeRepository.delete(foundStore);
    }

    public Page<StoreResponseDto> searchStore(String word, String sort, int page, int size) {
        return storeRepository.searchStore(word, sort, PageRequest.of(page - 1, size, Sort.by("storeId").ascending()));
    }

    public Store findVerifiedStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STORE_NOT_FOUND));
    }

    private StoreCategory findVerifiedStoreCategory(String storeCategoryId) {
        return storeCategoryRepository.findById(storeCategoryId)
                .orElseThrow(()->new BusinessLogicException(ExceptionCode.STORE_CATEGORY_NOT_FOUND));
    }
}
