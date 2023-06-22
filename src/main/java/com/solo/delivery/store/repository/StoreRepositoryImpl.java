package com.solo.delivery.store.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.solo.delivery.store.dto.QStoreResponseDto;
import com.solo.delivery.store.dto.StoreResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.solo.delivery.item.entity.QItem.item;
import static com.solo.delivery.store.entity.QStore.store;


@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<StoreResponseDto> searchStore(String word, Pageable pageable) {
        List<StoreResponseDto> content = queryFactory
                .select(new QStoreResponseDto(
                        store.storeId,
                        store.storeName,
                        store.address,
                        store.phone,
                        store.minimumOrderPrice,
                        store.storeCategory.storeCategory,
                        store.memberId
                ))
                .from(item)
                .join(item.store, store)
                .where(store.storeName.contains(word)
                        .or(item.itemName.contains(word)))
                .groupBy(store.storeId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(content, pageable, content.size());
    }
}
