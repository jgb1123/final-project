package com.solo.delivery.store.entity;

import com.solo.delivery.basetime.BaseTimeEntity;
import com.solo.delivery.item.entity.Item;
import com.solo.delivery.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Store extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long storeId;

    @Column
    private String storeName;

    @Column
    private String address;

    @Column
    private String phone;

    @Column
    private int minimumOrderPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_CATEGORY_ID")
    private StoreCategory storeCategory;

    @Builder.Default
    @OneToMany(mappedBy = "store")
    private List<Review> reviews = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "store")
    private List<Item> items = new ArrayList<>();

    public void changeItemCategory(StoreCategory storeCategory) {
        if(this.storeCategory != null) {
            this.storeCategory.getStores().remove(this);
        }
        this.storeCategory = storeCategory;
        if(!storeCategory.getStores().contains(this)) {
            storeCategory.addStore(this);
        }
    }

    public void addReview(Review review) {
        this.reviews.add(review);
        if(review.getStore() != this) {
            review.changeStore(this);
        }
    }

    public void addItem(Item item) {
        this.items.add(item);
        if(item.getStore() != this) {
            item.changeStore(this);
        }
    }
}
