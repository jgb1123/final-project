package com.solo.delivery.item;

import com.solo.delivery.dummy.ItemDummy;
import com.solo.delivery.dummy.StoreDummy;
import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.exception.ExceptionCode;
import com.solo.delivery.item.entity.Item;
import com.solo.delivery.item.repository.ItemRepository;
import com.solo.delivery.querydsl.config.QuerydslConfig;
import com.solo.delivery.store.entity.Store;
import com.solo.delivery.store.repository.StoreRepository;
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
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Test
    void saveTest() {
        Item item = ItemDummy.createItem1();

        Item savedItem = itemRepository.save(item);

        assertThat(savedItem.getItemName()).isEqualTo(item.getItemName());
        assertThat(savedItem.getPrice()).isEqualTo(item.getPrice());
    }

    @Test
    void findAllByStoreTest() {
        Item item1 = ItemDummy.createItem1();
        Item item2 = ItemDummy.createItem2();
        Store store = StoreDummy.createStore1();
        Store savedStore = storeRepository.save(store);
        item1.changeStore(savedStore);
        item2.changeStore(savedStore);
        Item savedItem1 = itemRepository.save(item1);
        Item savedItem2 = itemRepository.save(item2);

        Page<Item> itemPage = itemRepository.findAllByStore(savedStore, PageRequest.of(0, 10, Sort.by("itemId").ascending()));
        List<Item> items = itemPage.getContent();

        assertThat(items).contains(savedItem1);
        assertThat(items).contains(savedItem2);
    }

    @Test
    void updateTest() {
        Item modifiedItem = ItemDummy.createItem2();
        Item item = ItemDummy.createItem1();
        Item savedItem = itemRepository.save(item);
        Item foundItem = itemRepository.findById(savedItem.getItemId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ITEM_NOT_FOUND));

        foundItem.changeItemContent(modifiedItem);
        Item updatedItem = itemRepository.findById(foundItem.getItemId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ITEM_NOT_FOUND));

        assertThat(updatedItem.getItemName()).isEqualTo(modifiedItem.getItemName());
        assertThat(updatedItem.getInfo()).isEqualTo(modifiedItem.getInfo());
    }

    @Test
    void deleteTest() {
        Item item = ItemDummy.createItem1();
        Item savedItem = itemRepository.save(item);
        Item foundItem = itemRepository.findById(savedItem.getItemId()).orElse(null);

        itemRepository.delete(savedItem);
        Item foundItemAfterDelete = itemRepository.findById(savedItem.getItemId()).orElse(null);

        assertThat(foundItem).isNotNull();
        assertThat(foundItemAfterDelete).isNull();
    }
}
