package com.solo.delivery.item.service;

import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.exception.ExceptionCode;
import com.solo.delivery.item.entity.Item;
import com.solo.delivery.item.repository.ItemRepository;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.store.entity.Store;
import com.solo.delivery.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final StoreService storeService;
    private final MemberService memberService;

    public Item createItem(Item item, Long storeId, String email) {
        Member foundMember = memberService.findVerifiedMember(email);
        Store foundStore = storeService.findVerifiedStore(storeId);
        if(foundMember.getMemberId() != foundStore.getMemberId()) {
            throw new BusinessLogicException(ExceptionCode.ITEM_CANNOT_CHANGE);
        }
        item.changeStore(foundStore);
        return itemRepository.save(item);
    }

    public Item findItem(Long itemId) {
        return findVerifiedItem(itemId);
    }

    public Page<Item> findItems(Long storeId, Pageable pageable) {
        Store foundStore = storeService.findVerifiedStore(storeId);
        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        return itemRepository.findAllByStore(foundStore, pageable);
    }

    public Item updateItem(Long itemId, Item modifiedItem, String email) {
        Member foundMember = memberService.findVerifiedMember(email);
        Item foundItem = findVerifiedItem(itemId);
        if(foundItem.getStore().getMemberId() != foundMember.getMemberId()) {
            throw new BusinessLogicException(ExceptionCode.ITEM_CANNOT_CHANGE);
        }
        foundItem.changeItemContent(modifiedItem);
        return foundItem;
    }

    public void deleteItem(Long itemId, String email) {
        Member foundMember = memberService.findVerifiedMember(email);
        Item foundItem = findVerifiedItem(itemId);
        if(foundItem.getStore().getMemberId() != foundMember.getMemberId()) {
            throw new BusinessLogicException(ExceptionCode.ITEM_CANNOT_CHANGE);
        }
        itemRepository.delete(foundItem);
    }

    public Item findVerifiedItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ITEM_NOT_FOUND));
    }

    public void checkStockCnt(Integer itemCnt, Item item) {
        if(item.getStockCnt() < itemCnt) {
            throw new BusinessLogicException(ExceptionCode.OUT_OF_STOCK);
        }
    }
}
