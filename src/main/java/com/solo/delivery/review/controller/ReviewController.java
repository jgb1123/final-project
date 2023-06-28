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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewMapper reviewMapper;
    private final ReviewService reviewService;

    @PostMapping("/{storeId}")
    public ResponseEntity postReview(@Positive @PathVariable Long storeId,
                                     @Valid @RequestBody ReviewPostDto reviewPostDto,
                                     @AuthenticationPrincipal String email) {
        Review review = reviewMapper.reviewPostDtoToReview(reviewPostDto);
        reviewService.createReview(review, email, storeId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity getReviews(@Positive @PathVariable Long storeId,
                                     @PageableDefault(page = 1, size = 10, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Review> reviewPage = reviewService.findReviews(storeId, pageable);
        List<Review> reviews = reviewPage.getContent();
        List<ReviewResponseDto> reviewResponseDtos = reviewMapper.reviewsToReviewResponseDtos(reviews);
        return new ResponseEntity<>(new MultiResponseDto<>(reviewResponseDtos, reviewPage), HttpStatus.OK);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity patchReview(@Positive @PathVariable Long reviewId,
                                      @Valid @RequestBody ReviewPatchDto reviewPatchDto,
                                      @AuthenticationPrincipal String email) {
        Review modifiedReview = reviewMapper.reviewPatchDtoToReview(reviewPatchDto);
        reviewService.updateReview(modifiedReview, reviewId, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity deleteReview(@Positive @PathVariable Long reviewId,
                                       @AuthenticationPrincipal String email) {
        reviewService.deleteReview(email, reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
