package com.solo.delivery.store;

import com.google.gson.Gson;
import com.solo.delivery.dummy.StoreDummy;
import com.solo.delivery.exception.GlobalExceptionAdvice;
import com.solo.delivery.mail.service.MailService;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.security.config.SecurityConfig;
import com.solo.delivery.security.jwt.JwtTokenizer;
import com.solo.delivery.security.utils.CustomAuthorityUtils;
import com.solo.delivery.store.controller.StoreController;
import com.solo.delivery.store.dto.StorePatchDto;
import com.solo.delivery.store.dto.StorePostDto;
import com.solo.delivery.store.dto.StoreResponseDto;
import com.solo.delivery.store.entity.Store;
import com.solo.delivery.store.mapper.StoreMapper;
import com.solo.delivery.store.service.StoreService;
import com.solo.delivery.util.WithAuthMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreController.class)
@Import(SecurityConfig.class)
@MockBean({JpaMetamodelMappingContext.class, MemberService.class, JwtTokenizer.class, CustomAuthorityUtils.class, GlobalExceptionAdvice.class})
@AutoConfigureRestDocs
public class StoreControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;

    @MockBean
    private StoreMapper storeMapper;

    @Autowired
    private Gson gson;

    @Test
    @DisplayName("StoreController 생성")
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN"})
    void postStoreTest() throws Exception {
        StorePostDto storePostDto = StoreDummy.createPostDto();
        String content = gson.toJson(storePostDto);
        given(storeMapper.storePostDtoToStore(Mockito.any(StorePostDto.class)))
                .willReturn(new Store());
        given(storeService.createStore(Mockito.any(Store.class), Mockito.anyString()))
                .willReturn(new Store());

        ResultActions actions = mockMvc.perform(
                post("/api/v1/store")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .content(content)
        );

        actions.andExpect(status().isCreated())
                .andDo(document("post-store",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("storeName").type(JsonFieldType.STRING).description("상점 이름"),
                                        fieldWithPath("address").type(JsonFieldType.STRING).description("상점 주소"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("상점 전화번호"),
                                        fieldWithPath("minimumOrderPrice").type(JsonFieldType.NUMBER).description("최소 주문 금액"),
                                        fieldWithPath("storeCategoryId").type(JsonFieldType.STRING).description("카테고리 식별자"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        fieldWithPath("deliveryFee").type(JsonFieldType.NUMBER).description("배달비")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("StoreController 조회")
    void getStoreTest() throws Exception {
        Long storeId = 1L;
        StoreResponseDto storeResponseDto = StoreDummy.createResponseDto1();
        given(storeService.findStore(storeId))
                .willReturn(new Store());
        given(storeMapper.storeToStoreResponseDto(Mockito.any(Store.class)))
                .willReturn(storeResponseDto);

        ResultActions actions = mockMvc.perform(
                get("/api/v1/store/{storeId}", storeId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.storeId").value(storeResponseDto.getStoreId()))
                .andExpect(jsonPath("$.data.storeName").value(storeResponseDto.getStoreName()))
                .andExpect(jsonPath("$.data.address").value(storeResponseDto.getAddress()))
                .andExpect(jsonPath("$.data.phone").value(storeResponseDto.getPhone()))
                .andExpect(jsonPath("$.data.minimumOrderPrice").value(storeResponseDto.getMinimumOrderPrice()))
                .andExpect(jsonPath("$.data.storeCategory").value(storeResponseDto.getStoreCategory()))
                .andDo(document("get-store",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("상점 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data.storeId").type(JsonFieldType.NUMBER).description("상점 식별자"),
                                        fieldWithPath("data.storeName").type(JsonFieldType.STRING).description("상점 이름"),
                                        fieldWithPath("data.address").type(JsonFieldType.STRING).description("상점 주소"),
                                        fieldWithPath("data.phone").type(JsonFieldType.STRING).description("상점 전화번호"),
                                        fieldWithPath("data.minimumOrderPrice").type(JsonFieldType.NUMBER).description("최소 주문 금액"),
                                        fieldWithPath("data.storeCategory").type(JsonFieldType.STRING).description("상점 카테고리"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        fieldWithPath("data.starAvg").type(JsonFieldType.NUMBER).description("평균 별점"),
                                        fieldWithPath("data.totalOrderCnt").type(JsonFieldType.NUMBER).description("총 주문 수"),
                                        fieldWithPath("data.deliveryFee").type(JsonFieldType.NUMBER).description("배달비")
                                )
                        )
                        ));
    }

    @Test
    @DisplayName("StoreController 목록조회")
    void getStoresTest() throws Exception {
        String categoryId = "001";
        int page = 1;
        int size = 10;
        String sort = "totalOrderCnt,desc";
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", Integer.toString(page));
        queryParams.add("size", Integer.toString(size));
        queryParams.add("sort", sort);
        Store store1 = StoreDummy.createStore1();
        Store store2 = StoreDummy.createStore2();
        StoreResponseDto storeResponseDto1 = StoreDummy.createResponseDto1();
        StoreResponseDto storeResponseDto2 = StoreDummy.createResponseDto2();
        List<StoreResponseDto> responses = List.of(storeResponseDto1, storeResponseDto2);
        Page<Store> storePage = new PageImpl<>(List.of(store1, store2), PageRequest.of(page - 1, size,
                Sort.by("storeId").ascending()), 2);
        given(storeService.findStores(Mockito.anyString(), Mockito.any(Pageable.class)))
                .willReturn(storePage);
        given(storeMapper.storesToStoreResponseDtos(Mockito.anyList()))
                .willReturn(responses);

        ResultActions actions = mockMvc.perform(
                get("/api/v1/store/category/{categoryId}", categoryId)
                        .params(queryParams)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isOk())
                .andDo(document("get-stores",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 식별자")
                        ),
                        requestParameters(
                                List.of(
                                        parameterWithName("page").description("Page 번호"),
                                        parameterWithName("size").description("Page 크기"),
                                        parameterWithName("sort").description("Sort 기준")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data[].storeId").type(JsonFieldType.NUMBER).description("상점 식별자"),
                                        fieldWithPath("data[].storeName").type(JsonFieldType.STRING).description("상점 이름"),
                                        fieldWithPath("data[].address").type(JsonFieldType.STRING).description("상점 주소"),
                                        fieldWithPath("data[].phone").type(JsonFieldType.STRING).description("상점 전화번호"),
                                        fieldWithPath("data[].minimumOrderPrice").type(JsonFieldType.NUMBER).description("최소 주문 금액"),
                                        fieldWithPath("data[].storeCategory").type(JsonFieldType.STRING).description("상점 카테고리"),
                                        fieldWithPath("data[].memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        fieldWithPath("data[].starAvg").type(JsonFieldType.NUMBER).description("평균 별점"),
                                        fieldWithPath("data[].totalOrderCnt").type(JsonFieldType.NUMBER).description("총 주문 수"),
                                        fieldWithPath("data[].deliveryFee").type(JsonFieldType.NUMBER).description("배달비"),

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
    @DisplayName("StoreController 수정")
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN"})
    void patchStoreTest() throws Exception {
        Long storeId = 1L;
        StorePatchDto storePatchDto = StoreDummy.createPatchDto();
        String content = gson.toJson(storePatchDto);
        given(storeMapper.storePatchDtoToStore(Mockito.any(StorePatchDto.class)))
                .willReturn(new Store());

        ResultActions actions = mockMvc.perform(
                patch("/api/v1/store/{storeId}", storeId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .content(content)
        );

        actions.andExpect(status().isOk())
                .andDo(document("patch-store",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("상점 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("storeName").type(JsonFieldType.STRING).description("상점 이름"),
                                        fieldWithPath("address").type(JsonFieldType.STRING).description("상점 주소"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("상점 전화번호"),
                                        fieldWithPath("minimumOrderPrice").type(JsonFieldType.NUMBER).description("최소 주문 금액"),
                                        fieldWithPath("storeCategoryId").type(JsonFieldType.STRING).description("카테고리 식별자"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        fieldWithPath("deliveryFee").type(JsonFieldType.NUMBER).description("배달비")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("StoreController 삭제")
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN"})
    void deleteStoreTest() throws Exception {
        Long storeId = 1L;
        doNothing().when(storeService).deleteStore(Mockito.anyLong());

        ResultActions actions = mockMvc.perform(
                delete("/api/v1/store/{storeId}", storeId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        );

        actions.andExpect(status().isNoContent())
                .andDo(document("delete-store",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("상점 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("StoreController 검색조회")
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN"})
    void searchStoreTest() throws Exception {
        String word = "keyword";
        Integer minimumOrderPrice = 10000;
        Integer deliveryFee = 2000;
        int page = 1;
        int size = 10;
        String sort = "totalOrderCnt,desc";
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("word", word);
        queryParams.add("minimumOrderPrice", Integer.toString(minimumOrderPrice));
        queryParams.add("deliveryFee", Integer.toString(deliveryFee));
        queryParams.add("page", Integer.toString(page));
        queryParams.add("size", Integer.toString(size));
        queryParams.add("sort", sort);
        StoreResponseDto storeResponseDto1 = StoreDummy.createResponseDto1();
        StoreResponseDto storeResponseDto2 = StoreDummy.createResponseDto2();
        List<StoreResponseDto> responses = List.of(storeResponseDto1, storeResponseDto2);
        Page<StoreResponseDto> storePage = new PageImpl<>(List.of(storeResponseDto1, storeResponseDto2), PageRequest.of(page - 1, size,
                Sort.by("totalOrderCnt").descending()), 2);
        given(storeService.searchStore(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any(Pageable.class)))
                .willReturn(storePage);

        ResultActions actions = mockMvc.perform(
                get("/api/v1/store/search")
                        .params(queryParams)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isOk())
                .andDo(document("get-search-stores",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                List.of(
                                        parameterWithName("word").description("검색어"),
                                        parameterWithName("minimumOrderPrice").description("최소주문금액"),
                                        parameterWithName("deliveryFee").description("배달비"),
                                        parameterWithName("page").description("Page 번호"),
                                        parameterWithName("size").description("Page 크기"),
                                        parameterWithName("sort").description("Sort 기준")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data[].storeId").type(JsonFieldType.NUMBER).description("상점 식별자"),
                                        fieldWithPath("data[].storeName").type(JsonFieldType.STRING).description("상점 이름"),
                                        fieldWithPath("data[].address").type(JsonFieldType.STRING).description("상점 주소"),
                                        fieldWithPath("data[].phone").type(JsonFieldType.STRING).description("상점 전화번호"),
                                        fieldWithPath("data[].minimumOrderPrice").type(JsonFieldType.NUMBER).description("최소 주문 금액"),
                                        fieldWithPath("data[].storeCategory").type(JsonFieldType.STRING).description("상점 카테고리"),
                                        fieldWithPath("data[].memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        fieldWithPath("data[].starAvg").type(JsonFieldType.NUMBER).description("평균 별점"),
                                        fieldWithPath("data[].totalOrderCnt").type(JsonFieldType.NUMBER).description("총 주문 수"),
                                        fieldWithPath("data[].deliveryFee").type(JsonFieldType.NUMBER).description("배달비"),

                                        fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 사이즈"),
                                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("전체 건 수"),
                                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수")
                                )
                        )
                ));
    }
}
