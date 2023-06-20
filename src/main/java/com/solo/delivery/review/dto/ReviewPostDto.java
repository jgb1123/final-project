package com.solo.delivery.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewPostDto {
    @NotNull
    @Range(min = 1, max = 5, message = "별점은 1~5개 까지만 가능합니다")
    private Integer star;

    @NotBlank
    @Size(min = 5, max = 500, message = "내용은 5자이상 500자 이하이어야 합니다.")
    private String reviewContent;
}
