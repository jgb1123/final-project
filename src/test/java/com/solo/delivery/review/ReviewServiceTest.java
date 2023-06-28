package com.solo.delivery.review;

import com.solo.delivery.dummy.MemberDummy;
import com.solo.delivery.dummy.ReviewDummy;
import com.solo.delivery.dummy.StoreDummy;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.review.entity.Review;
import com.solo.delivery.review.repository.ReviewRepository;
import com.solo.delivery.review.service.ReviewService;
import com.solo.delivery.store.entity.Store;
import com.solo.delivery.store.service.StoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private StoreService storeService;

    @Mock
    private MemberService memberService;

    @Test
    void createReviewTest() {
        Review review = ReviewDummy.createReview1();
        Member member = MemberDummy.createMember1();
        Store store = StoreDummy.createStore1();
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member);
        given(storeService.findVerifiedStore(Mockito.anyLong()))
                .willReturn(store);
        given(reviewRepository.save(Mockito.any(Review.class)))
                .willReturn(review);

        Review savedReview = reviewService.createReview(review, "hgd@gmail.com", 1L);

        assertThat(savedReview.getMember().getEmail()).isEqualTo(member.getEmail());
        assertThat(savedReview.getStore().getStoreName()).isEqualTo(store.getStoreName());
        assertThat(savedReview.getReviewContent()).isEqualTo(review.getReviewContent());
    }

    @Test
    void findReviewsTest() {
        Review review1 = ReviewDummy.createReview1();
        Review review2 = ReviewDummy.createReview2();
        given(storeService.findVerifiedStore(Mockito.anyLong()))
                .willReturn(new Store());
        given(reviewRepository.findAllByStore(Mockito.any(Store.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(review1, review2), PageRequest.of(0, 10, Sort.by("reviewId").descending()), 2));

        Page<Review> reviewPage = reviewService.findReviews(1L, PageRequest.of(1, 10, Sort.by("reviewId").descending()));
        List<Review> reviews = reviewPage.getContent();

        assertThat(reviews).contains(review1);
        assertThat(reviews).contains(review2);
    }

    @Test
    void updateReviewTest() {
        Review modifiedReview = ReviewDummy.createReview2();
        Review review = ReviewDummy.createReview1();
        Member member = MemberDummy.createMember1();
        given(memberService.findVerifiedMember(Mockito.anyString()))
                .willReturn(member);
        given(reviewRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(review));

        Review updatedReview = reviewService.updateReview(modifiedReview, 1L, "hgd@gmail.com");

        assertThat(updatedReview.getReviewContent()).isEqualTo(modifiedReview.getReviewContent());
    }

    @Test
    void deleteReviewTest() {
        Review review = ReviewDummy.createReview1();
        Member member = MemberDummy.createMember1();
        review.changeMember(member);
        given(reviewRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(review));

        Assertions.assertDoesNotThrow(() -> reviewService.deleteReview("hgd@gmail.com", 1L));
    }
}
