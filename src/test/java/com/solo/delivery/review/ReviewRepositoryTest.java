package com.solo.delivery.review;

import com.solo.delivery.dummy.ReviewDummy;
import com.solo.delivery.dummy.StoreDummy;
import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.exception.ExceptionCode;
import com.solo.delivery.member.repository.MemberRepository;
import com.solo.delivery.querydsl.config.QuerydslConfig;
import com.solo.delivery.review.entity.Review;
import com.solo.delivery.review.repository.ReviewRepository;
import com.solo.delivery.store.entity.Store;
import com.solo.delivery.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
public class ReviewRepositoryTest {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    @DisplayName("ReviewRepository save")
    void saveTest() {
        Review review = ReviewDummy.createReview1();

        Review savedReview = reviewRepository.save(review);

        assertThat(savedReview.getStar()).isEqualTo(review.getStar());
        assertThat(savedReview.getReviewContent()).isEqualTo(review.getReviewContent());
    }

    @Test
    @DisplayName("ReviewRepository findAllByStore")
    void findAllByStoreTest() {
        Review review1 = ReviewDummy.createReview1();
        Review review2 = ReviewDummy.createReview2();
        Store store = StoreDummy.createStore1();
        Store savedStore = storeRepository.save(store);
        review1.changeStore(savedStore);
        review2.changeStore(savedStore);
        Review savedReview1 = reviewRepository.save(review1);
        Review savedReview2 = reviewRepository.save(review2);

        Page<Review> reviewPage = reviewRepository.findAllByStore(savedStore, PageRequest.of(0, 10, Sort.by("reviewId").ascending()));
        List<Review> reviews = reviewPage.getContent();

        assertThat(reviews).contains(savedReview1);
        assertThat(reviews).contains(savedReview2);
    }

    @Test
    @DisplayName("ReviewRepository update")
    void updateTest() {
        Review modifiedReview = ReviewDummy.createReview2();
        Review review = ReviewDummy.createReview1();
        Review savedReview = reviewRepository.save(review);
        Review foundReview = reviewRepository.findById(savedReview.getReviewId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));

        foundReview.changeReviewContent(modifiedReview);
        Review updatedReview = reviewRepository.findById(savedReview.getReviewId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));

        assertThat(updatedReview.getStar()).isEqualTo(modifiedReview.getStar());
        assertThat(updatedReview.getReviewContent()).isEqualTo(modifiedReview.getReviewContent());

    }

    @Test
    @DisplayName("ReviewRepository delete")
    void deleteTest() {
        Review review = ReviewDummy.createReview1();
        Review savedReview = reviewRepository.save(review);
        Review foundReview = reviewRepository.findById(savedReview.getReviewId()).orElse(null);

        reviewRepository.delete(savedReview);
        Review foundReviewAfterDelete = reviewRepository.findById(savedReview.getReviewId()).orElse(null);

        assertThat(foundReview).isNotNull();
        assertThat(foundReviewAfterDelete).isNull();
    }
}
