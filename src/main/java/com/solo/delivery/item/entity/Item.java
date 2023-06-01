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
public class Item extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long itemId;

    @Column
    private String itemName;

    @Column
    private int price;

    @Column
    private int sold_cnt;

    @Column
    private int stock_cnt;

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
}
