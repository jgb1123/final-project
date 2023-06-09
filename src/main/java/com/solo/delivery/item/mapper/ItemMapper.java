package com.solo.delivery.item.mapper;

import com.solo.delivery.item.dto.ItemPatchDto;
import com.solo.delivery.item.dto.ItemPostDto;
import com.solo.delivery.item.dto.ItemResponseDto;
import com.solo.delivery.item.entity.Item;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    public Item itemPostDtoToItem(ItemPostDto itemPostDto) {
        return Item.builder()
                .itemName(itemPostDto.getItemName())
                .price(itemPostDto.getPrice())
                .info(itemPostDto.getInfo())
                .stockCnt(itemPostDto.getStockCnt())
                .build();
    }

    public Item itemPatchDtoToItem(ItemPatchDto itemPatchDto) {
        return Item.builder()
                .itemName(itemPatchDto.getItemName())
                .price(itemPatchDto.getPrice())
                .stockCnt(itemPatchDto.getStockCnt())
                .info(itemPatchDto.getInfo())
                .build();
    }

    public ItemResponseDto itemToItemResponseDto(Item item) {
        return ItemResponseDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .soldCnt(item.getSoldCnt())
                .stockCnt(item.getStockCnt())
                .info(item.getInfo())
                .build();
    }

    public List<ItemResponseDto> itemsToItemResponseDtos(List<Item> items) {
        return items
                .stream()
                .map(item -> itemToItemResponseDto(item))
                .collect(Collectors.toList());
    }
}
