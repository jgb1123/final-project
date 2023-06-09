package com.solo.delivery.item.service;

import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.exception.ExceptionCode;
import com.solo.delivery.item.entity.Item;
import com.solo.delivery.item.repository.ItemRepository;
import com.solo.delivery.store.entity.Store;
import com.solo.delivery.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final StoreService storeService;

    public Item createItem(Item item, Long storeId) {
        Store foundStore = storeService.findVerifiedStore(storeId);
        item.changeStore(foundStore);
        return itemRepository.save(item);
    }

    public Item findItem(Long itemId) {
        return findVerifiedItem(itemId);
    }

    public Page<Item> findItems(Long storeId, int page, int size) {
        Store foundStore = storeService.findVerifiedStore(storeId);
        return itemRepository.findAllByStore(foundStore, PageRequest.of(page - 1, size, Sort.by("itemId").ascending()));
    }

    public Item updateItem(Long itemId, Item modifiedItem) {
        Item foundItem = findVerifiedItem(itemId);
        foundItem.changeItemContent(modifiedItem);
        return foundItem;
    }

    public void deleteItem(Long itemId) {
        Item foundItem = findVerifiedItem(itemId);
        itemRepository.delete(foundItem);
    }

    private Item findVerifiedItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ITEM_NOT_FOUND));
    }
}
