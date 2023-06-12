package com.solo.delivery.item;

import com.solo.delivery.dummy.ItemDummy;
import com.solo.delivery.dummy.StoreDummy;
import com.solo.delivery.item.entity.Item;
import com.solo.delivery.item.repository.ItemRepository;
import com.solo.delivery.item.service.ItemService;
import com.solo.delivery.store.entity.Store;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private StoreService storeService;

    @Test
    void createItemTest() {
        Item item = ItemDummy.createItem1();
        Store store = StoreDummy.createStore1();
        given(itemRepository.save(Mockito.any(Item.class)))
                .willReturn(item);
        given(storeService.findVerifiedStore(Mockito.anyLong()))
                .willReturn(store);

        Item savedItem = itemService.createItem(item, store.getStoreId());

        assertThat(item.getItemName()).isEqualTo(savedItem.getItemName());
        assertThat(item.getStore().getStoreName()).isEqualTo(store.getStoreName());
    }

    @Test
    void findItemsTest() {
        Item item1 = ItemDummy.createItem1();
        Item item2 = ItemDummy.createItem2();
        Store store = StoreDummy.createStore1();
        item1.changeStore(store);
        item2.changeStore(store);
        given(storeService.findVerifiedStore(Mockito.anyLong()))
                .willReturn(store);
        given(itemRepository.findAllByStore(Mockito.any(Store.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(item1, item2), PageRequest.of(0, 10, Sort.by("itemId").ascending()), 2));

        Page<Item> itemPage = itemService.findItems(store.getStoreId(), 1, 10);
        List<Item> items = itemPage.getContent();

        assertThat(items).contains(item1);
        assertThat(items).contains(item2);
    }

    @Test
    void updateItemTest() {
        Item modifiedItem = ItemDummy.createItem2();
        Item item = ItemDummy.createItem1();
        Store store = StoreDummy.createStore1();
        item.changeStore(store);
        given(itemRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(item));

        Item updatedItem = itemService.updateItem(item.getItemId(), modifiedItem);

        assertThat(updatedItem.getItemName()).isEqualTo(modifiedItem.getItemName());
        assertThat(updatedItem.getInfo()).isEqualTo(modifiedItem.getInfo());
    }

    @Test
    void deleteItemTest() {
        Item item = ItemDummy.createItem1();
        Store store = StoreDummy.createStore1();
        item.changeStore(store);
        given(itemRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(item));

        Assertions.assertDoesNotThrow(() -> itemService.deleteItem(item.getItemId()));
    }
}
