package com.solo.delivery.store;

import com.solo.delivery.dummy.StoreDummy;
import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.exception.ExceptionCode;
import com.solo.delivery.store.entity.Store;
import com.solo.delivery.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StoreRepositoryTest {
    @Autowired
    private StoreRepository storeRepository;

    @Test
    void saveTest() {
        Store store = StoreDummy.createStore1();

        Store savedStore = storeRepository.save(store);

        assertThat(store.getStoreName()).isEqualTo(savedStore.getStoreName());
        assertThat(store.getAddress()).isEqualTo(savedStore.getAddress());
    }

    @Test
    void findTest() {
        Store store = StoreDummy.createStore1();
        Store savedStore = storeRepository.save(store);

        Store foundStore = storeRepository.findById(savedStore.getStoreId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STORE_NOT_FOUND));

        assertThat(foundStore.getStoreId()).isEqualTo(savedStore.getStoreId());
        assertThat(foundStore.getStoreName()).isEqualTo(savedStore.getStoreName());
        assertThat(foundStore.getMinimumOrderPrice()).isEqualTo(savedStore.getMinimumOrderPrice());
    }

    @Test
    void findAllTest(){
        Store store1 = StoreDummy.createStore1();
        Store store2 = StoreDummy.createStore2();
        Store savedStore1 = storeRepository.save(store1);
        Store savedStore2 = storeRepository.save(store2);

        Page<Store> pageStores = storeRepository.findAll(PageRequest.of(0, 10,
                Sort.by("storeId").ascending()));
        List<Store> stores = pageStores.getContent();

        assertThat(stores).contains(savedStore1);
        assertThat(stores).contains(savedStore2);
    }

    @Test
    void updateTest() {
        Store store = StoreDummy.createStore1();
        Store modifiedStore = StoreDummy.createStore2();
        Store savedStore = storeRepository.save(store);
        Store foundStore = storeRepository.findById(savedStore.getStoreId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STORE_NOT_FOUND));

        foundStore.changeStoreContent(modifiedStore);
        Store updatedStore = storeRepository.findById(foundStore.getStoreId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STORE_NOT_FOUND));

        assertThat(updatedStore.getStoreName()).isEqualTo(modifiedStore.getStoreName());
        assertThat(updatedStore.getAddress()).isEqualTo(modifiedStore.getAddress());
    }

    @Test
    void deleteTest() {
        Store store = StoreDummy.createStore1();
        Store savedStore = storeRepository.save(store);
        Store foundStore = storeRepository.findById(savedStore.getStoreId()).orElse(null);

        storeRepository.delete(savedStore);
        Store foundStoreAfterDelete = storeRepository.findById(savedStore.getStoreId()).orElse(null);

        assertThat(foundStore).isNotNull();
        assertThat(foundStoreAfterDelete).isNull();
    }
}
