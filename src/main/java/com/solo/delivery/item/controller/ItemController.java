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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/item")
public class ItemController {
    private final ItemMapper itemMapper;
    private final ItemService itemService;

    @PostMapping("/{storeId}")
    public ResponseEntity postItem(@Positive @PathVariable Long storeId,
                                   @Valid @RequestBody ItemPostDto itemPostDto,
                                   @AuthenticationPrincipal String email) {
        Item item = itemMapper.itemPostDtoToItem(itemPostDto);
        Item savedItem = itemService.createItem(item, storeId, email);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity getItems(@Positive @PathVariable Long storeId,
                                   @PageableDefault(page = 1, size = 10, sort = "itemId", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Item> itemPage = itemService.findItems(storeId, pageable);
        List<Item> items = itemPage.getContent();
        List<ItemResponseDto> itemResponseDtos = itemMapper.itemsToItemResponseDtos(items);
        return new ResponseEntity<>(new MultiResponseDto<>(itemResponseDtos, itemPage), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity patchItem(@Positive @PathVariable Long itemId,
                                    @Valid @RequestBody ItemPatchDto itemPatchDto,
                                    @AuthenticationPrincipal String email) {
        Item modifiedItem = itemMapper.itemPatchDtoToItem(itemPatchDto);
        itemService.updateItem(itemId, modifiedItem, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity deleteItem(@Positive @PathVariable Long itemId,
                                     @AuthenticationPrincipal String email) {
        itemService.deleteItem(itemId, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
