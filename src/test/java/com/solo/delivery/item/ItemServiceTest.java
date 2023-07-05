package com.solo.delivery.item;

import com.solo.delivery.dummy.ItemDummy;
import com.solo.delivery.dummy.MemberDummy;
import com.solo.delivery.dummy.StoreDummy;
import com.solo.delivery.item.entity.Item;
import com.solo.delivery.item.repository.ItemRepository;
import com.solo.delivery.item.service.ItemService;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.store.entity.Store;
import com.solo.delivery.store.service.StoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
    private MemberService memberService;

    @Mock
    private StoreService storeService;

    @Test
    @DisplayName("ItemService createItem")
    void createItemTest() {
        Item item = ItemDummy.createItem1();
        Store store = StoreDummy.createStore1();
        Member member = MemberDummy.createMember1();
        given(itemRepository.save(Mockito.any(Item.class)))
                .willReturn(item);
        given(storeService.findVerifiedStore(Mockito.anyLong()))
                .willReturn(store);
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member);

        Item savedItem = itemService.createItem(item, store.getStoreId(), "hgd@gmail.com");

        assertThat(item.getItemName()).isEqualTo(savedItem.getItemName());
        assertThat(item.getStore().getStoreName()).isEqualTo(store.getStoreName());
    }

    @Test
    @DisplayName("ItemService findItems")
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

        Page<Item> itemPage = itemService.findItems(store.getStoreId(), PageRequest.of(1, 10, Sort.by("itemId").ascending()));
        List<Item> items = itemPage.getContent();

        assertThat(items).contains(item1);
        assertThat(items).contains(item2);
    }

    @Test
    @DisplayName("ItemService updateItem")
    void updateItemTest() {
        Item modifiedItem = ItemDummy.createItem2();
        Item item = ItemDummy.createItem1();
        Store store = StoreDummy.createStore1();
        Member member = MemberDummy.createMember1();
        item.changeStore(store);
        given(itemRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(item));
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member);

        Item updatedItem = itemService.updateItem(item.getItemId(), modifiedItem, "hgd@gmail.com");

        assertThat(updatedItem.getItemName()).isEqualTo(modifiedItem.getItemName());
        assertThat(updatedItem.getInfo()).isEqualTo(modifiedItem.getInfo());
    }

    @Test
    @DisplayName("ItemService deleteItem")
    void deleteItemTest() {
        Item item = ItemDummy.createItem1();
        Store store = StoreDummy.createStore1();
        Member member = MemberDummy.createMember1();
        item.changeStore(store);
        given(itemRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(item));
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member);

        Assertions.assertDoesNotThrow(() -> itemService.deleteItem(item.getItemId(), "hgd@gmail.com"));
    }
}
