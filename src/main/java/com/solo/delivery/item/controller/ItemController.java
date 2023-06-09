package com.solo.delivery.item.controller;

import com.solo.delivery.dto.MultiResponseDto;
import com.solo.delivery.item.dto.ItemPatchDto;
import com.solo.delivery.item.dto.ItemPostDto;
import com.solo.delivery.item.dto.ItemResponseDto;
import com.solo.delivery.item.entity.Item;
import com.solo.delivery.item.mapper.ItemMapper;
import com.solo.delivery.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
public class ItemController {
    private final ItemMapper itemMapper;
    private final ItemService itemService;

    @PostMapping("/{storeId}")
    public ResponseEntity postItem(@PathVariable Long storeId,
                                   @RequestBody ItemPostDto itemPostDto) {
        Item item = itemMapper.itemPostDtoToItem(itemPostDto);
        Item savedItem = itemService.createItem(item, storeId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity getItems(@PathVariable Long storeId,
                                   int page,
                                   int size) {
        Page<Item> pageItems = itemService.findItems(storeId, page, size);
        List<Item> items = pageItems.getContent();
        List<ItemResponseDto> itemResponseDtos = itemMapper.itemsToItemResponseDtos(items);
        return new ResponseEntity<>(new MultiResponseDto<>(itemResponseDtos, pageItems), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity patchItem(@PathVariable Long itemId,
                                    @RequestBody ItemPatchDto itemPatchDto) {
        Item modifiedItem = itemMapper.itemPatchDtoToItem(itemPatchDto);
        itemService.updateItem(itemId, modifiedItem);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
