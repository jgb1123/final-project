package com.solo.delivery.store.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreCategory {
    @Id
    private String itemCategoryId;

    @Column
    private String itemCategory;

    @Builder.Default
    @OneToMany(mappedBy = "storeCategory")
    private List<Store> stores = new ArrayList<>();

    public void addStore(Store store) {
        this.stores.add(store);
        if(store.getStoreCategory() != this) {
            store.changeItemCategory(this);
        }
    }
}
