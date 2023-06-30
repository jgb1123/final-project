package com.solo.delivery.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {
    private Long reviewId;
    private Integer star;
    private String reviewContent;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String nickname;
}
