package com.solo.delivery.review.service;

import com.solo.delivery.exception.BusinessLogicException;
import com.solo.delivery.exception.ExceptionCode;
import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.review.entity.Review;
import com.solo.delivery.review.repository.ReviewRepository;
import com.solo.delivery.store.entity.Store;
import com.solo.delivery.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberService memberService;
    private final StoreService storeService;

    public Review createReview(Review review, String email, Long storeId) {
        Member foundMember = memberService.findVerifiedMember(email);
        Store foundStore = storeService.findVerifiedStore(storeId);
        review.changeMember(foundMember);
        review.changeStore(foundStore);
        foundStore.updateStarAvg();
        return reviewRepository.save(review);
    }

    public Page<Review> findReviews(Long storeId, Pageable pageable) {
        Store foundStore = storeService.findVerifiedStore(storeId);
        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        return reviewRepository.findAllByStore(foundStore, pageable);
    }

    public Review updateReview(Review modifiedReview, Long reviewId, String email) {
        Member foundMember = memberService.findVerifiedMember(email);
        Review foundReview = findVerifiedReview(reviewId);
        checkMember(email, foundMember.getEmail());
        foundReview.changeReviewContent(modifiedReview);
        return foundReview;
    }

    public void deleteReview(String email, Long reviewId) {
        Review foundReview = findVerifiedReview(reviewId);
        checkMember(email, foundReview.getMember().getEmail());
        reviewRepository.delete(foundReview);
    }

    public Review findVerifiedReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));
    }

    public void checkMember(String email, String memberEmail) {
        if(!email.equals(memberEmail)) {
            throw new BusinessLogicException(ExceptionCode.REVIEW_CANNOT_CHANGE);
        }
    }
}
