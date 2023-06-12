package com.solo.delivery.dummy;

import com.solo.delivery.review.dto.ReviewPatchDto;
import com.solo.delivery.review.dto.ReviewPostDto;
import com.solo.delivery.review.dto.ReviewResponseDto;
import com.solo.delivery.review.entity.Review;

import java.time.LocalDateTime;

public interface ReviewDummy {
    static Review createReview1() {
        return Review.builder()
                .star(5)
                .reviewContent("맛있어요")
                .build();
    }

    static Review createReview2() {
        return Review.builder()
                .star(3)
                .reviewContent("또 주문할게요")
                .build();
    }

    static ReviewPostDto createPostDto() {
        return ReviewPostDto.builder()
                .star(5)
                .reviewContent("맛있어요")
                .build();
    }

    static ReviewPatchDto createPatchDto() {
        return ReviewPatchDto.builder()
                .star(4)
                .reviewContent("덜 맛있어요")
                .build();
    }

    static ReviewResponseDto createResponseDto1() {
        return ReviewResponseDto.builder()
                .reviewId(1L)
                .star(5)
                .reviewContent("맛있어요")
                .createdAt(LocalDateTime.of(2023, 6, 12, 11, 0, 0))
                .modifiedAt(LocalDateTime.of(2023, 6, 12, 11, 0, 0))
                .build();
    }

    static ReviewResponseDto createResponseDto2() {
        return ReviewResponseDto.builder()
                .reviewId(2L)
                .star(3)
                .reviewContent("또 주문할게요")
                .createdAt(LocalDateTime.of(2023, 6, 12, 12, 0, 0))
                .modifiedAt(LocalDateTime.of(2023, 6, 12, 12, 0, 0))
                .build();
    }

}
