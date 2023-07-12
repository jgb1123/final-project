package com.solo.delivery.store.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.solo.delivery.querydsl.util.QueryDslUtil;
import com.solo.delivery.store.dto.QStoreResponseDto;
import com.solo.delivery.store.dto.StoreResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.solo.delivery.item.entity.QItem.item;
import static com.solo.delivery.store.entity.QStore.store;
import static org.springframework.util.ObjectUtils.isEmpty;


@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<StoreResponseDto> searchStore(String word, Integer minimumOrderPrice, Pageable pageable) {
        List<StoreResponseDto> content = queryFactory
                .select(new QStoreResponseDto(
                        store.storeId,
                        store.storeName,
                        store.address,
                        store.phone,
                        store.minimumOrderPrice,
                        store.storeCategory.storeCategory,
                        store.memberId,
                        store.starAvg,
                        store.totalOrderCnt
                ))
                .from(store)
                .leftJoin(store.items, item)
                .where(
                        containWord(word),
                        loeMinimumOrderPrice(minimumOrderPrice)
                )
                .distinct()
                .orderBy(createOrderSpecifier(pageable).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    private List<OrderSpecifier> createOrderSpecifier(Pageable pageable) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        if(!isEmpty(pageable.getSort())) {
            for(Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch(order.getProperty()) {
                    case "starAvg":
                        OrderSpecifier<?> orderStarAvg = QueryDslUtil.getSortedColumn(direction, store, "starAvg");
                        orderSpecifiers.add(orderStarAvg);
                        break;
                    case "totalOrderCnt":
                        OrderSpecifier<?> orderTotalOrderCnt = QueryDslUtil.getSortedColumn(direction, store, "totalOrderCnt");
                        orderSpecifiers.add(orderTotalOrderCnt);
                        break;
                    case "createdAt":
                        OrderSpecifier<?> orderCreatedAt = QueryDslUtil.getSortedColumn(direction, store, "createdAt");
                        orderSpecifiers.add(orderCreatedAt);
                        break;
                    case "minimumOrderPrice":
                        OrderSpecifier<?> orderMinimumOrderPrice = QueryDslUtil.getSortedColumn(direction, store, "minimumOrderPrice");
                        orderSpecifiers.add(orderMinimumOrderPrice);
                        break;
                    default:
                        break;
                }
            }
        }
        return orderSpecifiers;
    }
    private BooleanExpression containWord(String word) {
        if(word == null || word.isEmpty()) return null;
        return store.storeName.contains(word)
                .or(item.itemName.contains(word));
    }

    private BooleanExpression loeMinimumOrderPrice(Integer minimumOrderPrice) {
        if(minimumOrderPrice == null) return null;
        return store.minimumOrderPrice.loe(minimumOrderPrice);
    }
}
