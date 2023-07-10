package com.solo.delivery.review;

import com.google.gson.Gson;
import com.solo.delivery.dummy.ReviewDummy;
import com.solo.delivery.mail.service.MailService;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.review.controller.ReviewController;
import com.solo.delivery.review.dto.ReviewPatchDto;
import com.solo.delivery.review.dto.ReviewPostDto;
import com.solo.delivery.review.dto.ReviewResponseDto;
import com.solo.delivery.review.entity.Review;
import com.solo.delivery.review.mapper.ReviewMapper;
import com.solo.delivery.review.service.ReviewService;
import com.solo.delivery.security.config.SecurityConfig;
import com.solo.delivery.security.jwt.JwtTokenizer;
import com.solo.delivery.security.utils.CustomAuthorityUtils;
import com.solo.delivery.util.WithAuthMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
@Import(SecurityConfig.class)
@MockBean({JpaMetamodelMappingContext.class, MemberService.class, JwtTokenizer.class, CustomAuthorityUtils.class, MailService.class})
@AutoConfigureRestDocs
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private ReviewMapper reviewMapper;

    @Autowired
    private Gson gson;

    @Test
    @DisplayName("ReviewController 생성")
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN"})
    void postReviewTest() throws Exception {
        Long storeId = 1L;
        Review review = ReviewDummy.createReview1();
        ReviewPostDto reviewPostDto = ReviewDummy.createPostDto();
        String content = gson.toJson(reviewPostDto);
        given(reviewMapper.reviewPostDtoToReview(Mockito.any(ReviewPostDto.class)))
                .willReturn(new Review());
        given(reviewService.createReview(Mockito.any(Review.class), Mockito.anyString(), Mockito.anyLong()))
                .willReturn(review);

        ResultActions actions = mockMvc.perform(
                post("/api/v1/review/{storeId}", storeId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .content(content)
        );

        actions.andExpect(status().isCreated())
                .andDo(document("post-review",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("상점 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("star").type(JsonFieldType.NUMBER).description("별점"),
                                        fieldWithPath("reviewContent").type(JsonFieldType.STRING).description("리뷰 내용")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("ReviewController 목록조회")
    void getReviewsTest() throws Exception {
        Long storeId = 1L;
        int page = 1;
        int size = 1;
        String sort = "reviewId,desc";
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", Integer.toString(page));
        queryParams.add("size", Integer.toString(size));
        queryParams.add("sort", sort);
        Review review1 = ReviewDummy.createReview1();
        Review review2 = ReviewDummy.createReview2();
        ReviewResponseDto reviewResponseDto1 = ReviewDummy.createResponseDto1();
        ReviewResponseDto reviewResponseDto2 = ReviewDummy.createResponseDto2();
        List<ReviewResponseDto> responses = List.of(reviewResponseDto1, reviewResponseDto2);
        Page<Review> reviewPage = new PageImpl<>(List.of(review1, review2), PageRequest.of(page - 1, size,
                Sort.by("reviewId").ascending()), 2);
        given(reviewService.findReviews(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .willReturn(reviewPage);
        given(reviewMapper.reviewsToReviewResponseDtos(Mockito.anyList()))
                .willReturn(responses);

        ResultActions actions = mockMvc.perform(
                get("/api/v1/review/{storeId}", storeId)
                        .params(queryParams)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isOk())
                .andDo(document("get-reviews",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("상품 식별자")
                        ),
                        requestParameters(
                                List.of(
                                        parameterWithName("page").description("Page 번호"),
                                        parameterWithName("size").description("Size 크기"),
                                        parameterWithName("sort").description("Sort 기준")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data[].reviewId").type(JsonFieldType.NUMBER).description("리뷰 식별자"),
                                        fieldWithPath("data[].star").type(JsonFieldType.NUMBER).description("별점"),
                                        fieldWithPath("data[].reviewContent").type(JsonFieldType.STRING).description("리뷰 내용"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("작성일"),
                                        fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING).description("수정일"),
                                        fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("별명"),

                                        fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 사이즈"),
                                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("전체 건 수"),
                                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("ReviewController 수정")
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN"})
    void patchReviewTest() throws Exception {
        Long reviewId = 1L;
        Review review = ReviewDummy.createReview1();
        ReviewPatchDto reviewPatchDto = ReviewDummy.createPatchDto();
        String content = gson.toJson(reviewPatchDto);
        given(reviewMapper.reviewPatchDtoToReview(Mockito.any(ReviewPatchDto.class)))
                .willReturn(new Review());

        ResultActions actions = mockMvc.perform(
                patch("/api/v1/review/{reviewId}", reviewId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .content(content)
        );

        actions.andExpect(status().isOk())
                .andDo(document("patch-review",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("star").type(JsonFieldType.NUMBER).description("별점").optional(),
                                        fieldWithPath("reviewContent").type(JsonFieldType.STRING).description("리뷰 내용").optional()
                                )
                        )
                ));
    }

    @Test
    @DisplayName("ReviewController 삭제")
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN"})
    void deleteReviewTest() throws Exception {
        Long reviewId = 1L;
        doNothing().when(reviewService).deleteReview(Mockito.anyString(), Mockito.anyLong());

        ResultActions actions = mockMvc.perform(
                delete("/api/v1/review/{reviewId}", reviewId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        );

        actions.andExpect(status().isNoContent())
                .andDo(document("delete-review",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 식별자")
                        )
                ));
    }
}
