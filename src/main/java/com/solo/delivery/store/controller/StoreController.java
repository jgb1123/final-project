package com.solo.delivery.store.controller;

import com.solo.delivery.dto.MultiResponseDto;
import com.solo.delivery.dto.SingleResponseDto;
import com.solo.delivery.store.dto.StorePatchDto;
import com.solo.delivery.store.dto.StorePostDto;
import com.solo.delivery.store.dto.StoreResponseDto;
import com.solo.delivery.store.entity.Store;
import com.solo.delivery.store.mapper.StoreMapper;
import com.solo.delivery.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store")
public class StoreController {
    private final StoreMapper storeMapper;
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity postStore(@RequestBody StorePostDto storePostDto) {
        Store store = storeMapper.storePostDtoToStore(storePostDto);
        Store savedStore = storeService.createStore(store, storePostDto.getStoreCategoryId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity getStore(@PathVariable Long storeId) {
        Store foundStore = storeService.findStore(storeId);
        StoreResponseDto storeResponseDto = storeMapper.storeToStoreResponseDto(foundStore);
        return new ResponseEntity<>(new SingleResponseDto<>(storeResponseDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getStores(@RequestParam int page,
                                    @RequestParam int size) {
        Page<Store> storePage = storeService.findStores(page, size);
        List<Store> stores = storePage.getContent();
        List<StoreResponseDto> storeResponseDtos = storeMapper.storesToStoreResponseDtos(stores);
        return new ResponseEntity<>(new MultiResponseDto<>(storeResponseDtos, storePage), HttpStatus.OK);
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity patchStore(@PathVariable Long storeId,
                                     @RequestBody StorePatchDto storePatchDto) {
        Store modifiedStore = storeMapper.storePatchDtoToStore(storePatchDto);
        storeService.updateStore(storeId, modifiedStore, storePatchDto.getStoreCategoryId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity deleteStore(@PathVariable Long storeId) {
        storeService.deleteStore(storeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
