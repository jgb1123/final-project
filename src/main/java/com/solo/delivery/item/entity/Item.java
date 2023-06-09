package com.solo.delivery.item.entity;

import com.solo.delivery.basetime.BaseTimeEntity;
import com.solo.delivery.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column
    private String itemName;

    @Column
    @Builder.Default
    private Integer price = 0;

    @Column
    @Builder.Default
    private Integer soldCnt = 0;

    @Column
    @Builder.Default
    private Integer stockCnt = 0;

    @Column
    private String info;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID")
    private Store store;

    public void changeStore(Store store) {
        if(this.store != null) {
            this.store.getItems().remove(this);
        }
        this.store = store;
        if(!store.getItems().contains(this)) {
            store.addItem(this);
        }
    }

    public void changeItemContent(Item item) {
        if(item.getItemName() != null) this.itemName = item.getItemName();
        if(item.getPrice() != null) this.price = item.getPrice();
        if(item.getStockCnt() != null) this.stockCnt = item.getStockCnt();
        if(item.getInfo() != null) this.info = item.getInfo();
    }
}
