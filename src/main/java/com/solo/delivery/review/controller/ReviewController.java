package com.solo.delivery.review.controller;

import com.solo.delivery.dto.MultiResponseDto;
import com.solo.delivery.review.dto.ReviewPatchDto;
import com.solo.delivery.review.dto.ReviewPostDto;
import com.solo.delivery.review.dto.ReviewResponseDto;
import com.solo.delivery.review.entity.Review;
import com.solo.delivery.review.mapper.ReviewMapper;
import com.solo.delivery.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewMapper reviewMapper;
    private final ReviewService reviewService;

    @PostMapping("/{storeId}")
    public ResponseEntity postReview(@PathVariable Long storeId,
                                     @RequestBody ReviewPostDto reviewPostDto,
                                     @AuthenticationPrincipal String email) {
        Review review = reviewMapper.reviewPostDtoToReview(reviewPostDto);
        reviewService.createReview(review, email, storeId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity getReviews(@PathVariable Long storeId,
                                     int page,
                                     int size) {
        Page<Review> pageReviews = reviewService.findReviews(storeId, page, size);
        List<Review> reviews = pageReviews.getContent();
        List<ReviewResponseDto> reviewResponseDtos = reviewMapper.reviewsToReviewResponseDtos(reviews);
        return new ResponseEntity<>(new MultiResponseDto<>(reviewResponseDtos, pageReviews), HttpStatus.OK);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity patchReview(@PathVariable Long reviewId,
                                      @RequestBody ReviewPatchDto reviewPatchDto,
                                      @AuthenticationPrincipal String email) {
        Review modifiedReview = reviewMapper.reviewPatchDtoToReview(reviewPatchDto);
        reviewService.updateReview(modifiedReview, reviewId, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity deleteReview(@PathVariable Long reviewId,
                                       @AuthenticationPrincipal String email) {
        reviewService.deleteReview(email, reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
