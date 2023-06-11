package com.solo.delivery.review.entity;

import com.solo.delivery.basetime.BaseTimeEntity;
import com.solo.delivery.member.entity.Member;
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
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column
    private Integer star;

    @Column
    private String reviewContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID")
    private Store store;

    public void changeMember(Member member) {
        if(this.member != null) {
            this.member.getReviews().remove(this);
        }
        this.member = member;
        if(!member.getReviews().contains(this)) {
            member.addReview(this);
        }
    }

    public void changeStore(Store store) {
        if(this.store != null) {
            this.store.getReviews().remove(this);
        }
        this.store = store;
        if(!store.getReviews().contains(this)) {
            store.addReview(this);
        }
    }

    public void changeReviewContent(Review review) {
        if(review.getStar() != null) this.star = review.getStar();
        if(review.getReviewContent() != null) this.reviewContent = review.getReviewContent();
    }
}
