package com.solo.delivery.store;

import com.solo.delivery.dummy.StoreDummy;
import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.store.entity.Store;
import com.solo.delivery.store.entity.StoreCategory;
import com.solo.delivery.store.repository.StoreCategoryRepository;
import com.solo.delivery.store.repository.StoreRepository;
import com.solo.delivery.store.service.StoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {
    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreCategoryRepository storeCategoryRepository;

    @Test
    void createStoreTest() {
        Store store = StoreDummy.createStore1();
        StoreCategory storeCategory = StoreDummy.createStoreCategory();
        given(storeCategoryRepository.findById(anyString()))
                .willReturn(Optional.of(storeCategory));
        given(storeRepository.save(Mockito.any(Store.class)))
                .willReturn(store);

        Store savedStore = storeService.createStore(store, "001");

        assertThat(savedStore.getStoreName()).isEqualTo(store.getStoreName());
        assertThat(savedStore.getAddress()).isEqualTo(store.getAddress());
        assertThat(savedStore.getStoreCategory()).isNotNull();
//        assertThat(savedStore.getStoreCategory().getStoreCategoryId()).isNotEqualTo(inputStore.getStoreCategory().getStoreCategoryId());
    }

    @Test
    void findStoreTest() {
        Store store = StoreDummy.createStore1();
        given(storeRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(store));

        Store foundStore = storeService.findStore(1L);

        assertThat(store.getStoreName()).isEqualTo(foundStore.getStoreName());
        assertThat(store.getAddress()).isEqualTo(foundStore.getAddress());
    }

    @Test
    void findStoresTest() {
        Store store1 = StoreDummy.createStore1();
        Store store2 = StoreDummy.createStore2();
        given(storeRepository.findAll(Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(store1, store2), PageRequest.of(0, 10, Sort.by("storeId").ascending()), 2));

        Page<Store> pageStores = storeService.findStores(1, 10);

        assertThat(pageStores.getContent()).contains(store1);
        assertThat(pageStores.getContent()).contains(store2);
        assertThat(pageStores.getTotalElements()).isEqualTo(2);
    }

    @Test
    void updateStoreTest() {
        Store modifiedStore = StoreDummy.createStore1();
        Store store = StoreDummy.createStore2();
        StoreCategory storeCategory = StoreDummy.createStoreCategory();
        given(storeRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(store));
        given(storeCategoryRepository.findById(anyString()))
                .willReturn(Optional.of(storeCategory));

        Store updatedStore = storeService.updateStore(2L, modifiedStore, "001");

        assertThat(updatedStore.getStoreName()).isEqualTo(modifiedStore.getStoreName());
        assertThat(updatedStore.getMinimumOrderPrice()).isEqualTo(modifiedStore.getMinimumOrderPrice());
    }

    @Test
    void deleteStoreTest() {
        Store store = StoreDummy.createStore1();
        given(storeRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(store));
        doNothing().when(storeRepository).delete(Mockito.any(Store.class));

        Assertions.assertDoesNotThrow(() -> storeService.deleteStore(1L));
    }
}
