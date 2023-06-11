package com.solo.delivery.review.mapper;

import com.solo.delivery.review.dto.ReviewPatchDto;
import com.solo.delivery.review.dto.ReviewPostDto;
import com.solo.delivery.review.dto.ReviewResponseDto;
import com.solo.delivery.review.entity.Review;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewMapper {
    public Review reviewPostDtoToReview(ReviewPostDto reviewPostDto) {
        return Review.builder()
                .star(reviewPostDto.getStar())
                .reviewContent(reviewPostDto.getReviewContent())
                .build();
    }

    public Review reviewPatchDtoToReview(ReviewPatchDto reviewPatchDto) {
        return Review.builder()
                .star(reviewPatchDto.getStar())
                .reviewContent(reviewPatchDto.getReviewContent())
                .build();
    }

    public ReviewResponseDto reviewToReviewResponseDto(Review review){
        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .star(review.getStar())
                .reviewContent(review.getReviewContent())
                .createdAt(review.getCreatedAt())
                .modifiedAt(review.getModifiedAt())
                .build();
    }

    public List<ReviewResponseDto> reviewsToReviewResponseDtos(List<Review> reviews) {
        return reviews
                .stream()
                .map(review -> reviewToReviewResponseDto(review))
                .collect(Collectors.toList());
    }

}
