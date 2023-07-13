package com.solo.delivery.store;

import com.solo.delivery.dummy.ItemDummy;
import com.solo.delivery.dummy.StoreDummy;
import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.exception.ExceptionCode;
import com.solo.delivery.item.entity.Item;
import com.solo.delivery.item.repository.ItemRepository;
import com.solo.delivery.querydsl.config.QuerydslConfig;
import com.solo.delivery.store.dto.StoreResponseDto;
import com.solo.delivery.store.entity.Store;
import com.solo.delivery.store.entity.StoreCategory;
import com.solo.delivery.store.repository.StoreCategoryRepository;
import com.solo.delivery.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
public class StoreRepositoryTest {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private StoreCategoryRepository storeCategoryRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("StoreRepository save")
    void saveTest() {
        Store store = StoreDummy.createStore1();

        Store savedStore = storeRepository.save(store);

        assertThat(store.getStoreName()).isEqualTo(savedStore.getStoreName());
        assertThat(store.getAddress()).isEqualTo(savedStore.getAddress());
    }

    @Test
    @DisplayName("StoreRepository findById")
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
    @DisplayName("StoreRepository findAllByStoreCategoryStoreCategoryIdStartingWith")
    void findAllTest(){
        StoreCategory storeCategory = StoreDummy.createStoreCategory();
        StoreCategory savedStoreCategory = storeCategoryRepository.save(storeCategory);
        Store store1 = StoreDummy.createStore1();
        Store store2 = StoreDummy.createStore2();
        store1.changeStoreCategory(savedStoreCategory);
        store2.changeStoreCategory(savedStoreCategory);
        Store savedStore1 = storeRepository.save(store1);
        Store savedStore2 = storeRepository.save(store2);

        Page<Store> storePage = storeRepository.findAllByStoreCategoryStoreCategoryIdStartingWith(savedStoreCategory.getStoreCategoryId(), PageRequest.of(0, 10,
                Sort.by("storeId").ascending()));
        List<Store> stores = storePage.getContent();

        assertThat(stores).contains(savedStore1);
        assertThat(stores).contains(savedStore2);
    }

    @Test
    @DisplayName("StoreRepository update")
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
    @DisplayName("StoreRepository delete")
    void deleteTest() {
        Store store = StoreDummy.createStore1();
        Store savedStore = storeRepository.save(store);
        Store foundStore = storeRepository.findById(savedStore.getStoreId()).orElse(null);

        storeRepository.delete(savedStore);
        Store foundStoreAfterDelete = storeRepository.findById(savedStore.getStoreId()).orElse(null);

        assertThat(foundStore).isNotNull();
        assertThat(foundStoreAfterDelete).isNull();
    }

    @Test
    @DisplayName("StoreRepository search")
    void searchWordTest() {
        StoreCategory storeCategory = StoreDummy.createStoreCategory();
        StoreCategory savedStoreCategory = storeCategoryRepository.save(storeCategory);
        Store store1 = StoreDummy.createStore1();
        store1.changeStoreCategory(savedStoreCategory);
        Store savedStore1 = storeRepository.save(store1);
        Store store2 = StoreDummy.createStore2();
        store2.changeStoreCategory(savedStoreCategory);
        Store savedStore2 = storeRepository.save(store2);
        storeRepository.flush();
        Item item1 = ItemDummy.createItem1();
        Item item2 = ItemDummy.createItem2();
        Item item3 = ItemDummy.createItem3();
        item1.changeStore(savedStore1);
        item2.changeStore(savedStore1);
        item3.changeStore(savedStore2);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        Page<StoreResponseDto> storeResponseDtoPage1 = storeRepository.searchStore("김치", 20000, PageRequest.of(0, 10, Sort.by("starAvg").descending()));
        Page<StoreResponseDto> storeResponseDtoPage2 = storeRepository.searchStore("김치볶음밥", null, PageRequest.of(0, 10, Sort.by("totalOrderCnt").descending()));
        Page<StoreResponseDto> storeResponseDtoPage3 = storeRepository.searchStore("짜장면", null, PageRequest.of(0, 10, Sort.by("totalOrderCnt").descending()));
        Page<StoreResponseDto> storeResponseDtoPage4 = storeRepository.searchStore("김치", 10000, PageRequest.of(0, 10, Sort.by("starAvg").descending()));

        assertThat(storeResponseDtoPage1.getContent().get(0).getStoreName()).isEqualTo("백년아구찜");    // Sort Test
        assertThat(storeResponseDtoPage1.getTotalElements()).isEqualTo(2);  // word Test
        assertThat(storeResponseDtoPage2.getTotalElements()).isEqualTo(1);  // word Test
        assertThat(storeResponseDtoPage3.getTotalElements()).isEqualTo(1);  // word Test
        assertThat(storeResponseDtoPage4.getTotalElements()).isEqualTo(1);  // minimumOrderPrice Test
    }
}
