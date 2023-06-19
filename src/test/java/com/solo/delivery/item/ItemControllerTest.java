package com.solo.delivery.item;

import com.google.gson.Gson;
import com.solo.delivery.dummy.ItemDummy;
import com.solo.delivery.item.controller.ItemController;
import com.solo.delivery.item.dto.ItemPatchDto;
import com.solo.delivery.item.dto.ItemPostDto;
import com.solo.delivery.item.dto.ItemResponseDto;
import com.solo.delivery.item.entity.Item;
import com.solo.delivery.item.mapper.ItemMapper;
import com.solo.delivery.item.service.ItemService;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.security.config.SecurityConfig;
import com.solo.delivery.security.jwt.JwtTokenizer;
import com.solo.delivery.security.utils.CustomAuthorityUtils;
import com.solo.delivery.util.WithAuthMember;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

@WebMvcTest(ItemController.class)
@Import(SecurityConfig.class)
@MockBean({JpaMetamodelMappingContext.class, MemberService.class, JwtTokenizer.class, CustomAuthorityUtils.class})
@AutoConfigureRestDocs
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemMapper itemMapper;

    @Autowired
    private Gson gson;

    @Test
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN"})
    void postItemTest() throws Exception {
        Long storeId = 1L;
        Item item = ItemDummy.createItem1();
        ItemPostDto itemPostDto = ItemDummy.createPostDto();
        String content = gson.toJson(itemPostDto);
        given(itemMapper.itemPostDtoToItem(Mockito.any(ItemPostDto.class)))
                .willReturn(new Item());
        given(itemService.createItem(Mockito.any(Item.class), Mockito.anyLong(), Mockito.anyString()))
                .willReturn(item);

        ResultActions actions = mockMvc.perform(
                post("/api/v1/item/{storeId}", storeId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .content(content)
        );

        actions.andExpect(status().isCreated())
                .andDo(document("post-item",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("상점 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("itemName").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                        fieldWithPath("stockCnt").type(JsonFieldType.NUMBER).description("재고량"),
                                        fieldWithPath("info").type(JsonFieldType.STRING).description("상품 설명")
                                )
                        )
                ));
    }

    @Test
    void getItemsTest() throws Exception {
        Long storeId = 1L;
        int page = 1;
        int size = 10;
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", Integer.toString(page));
        queryParams.add("size", Integer.toString(size));
        Item item1 = ItemDummy.createItem1();
        Item item2 = ItemDummy.createItem2();
        ItemResponseDto itemResponseDto1 = ItemDummy.createResponseDto1();
        ItemResponseDto itemResponseDto2 = ItemDummy.createResponseDto2();
        List<ItemResponseDto> responses = List.of(itemResponseDto1, itemResponseDto2);
        Page<Item> itemPage = new PageImpl<>(List.of(item1, item2), PageRequest.of(page - 1, size,
                Sort.by("itemId").ascending()), 2);
        given(itemService.findItems(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(itemPage);
        given(itemMapper.itemsToItemResponseDtos(Mockito.anyList()))
                .willReturn(responses);

        ResultActions actions = mockMvc.perform(
                get("/api/v1/item/{storeId}", storeId)
                        .params(queryParams)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        actions.andExpect(status().isOk())
                .andDo(document("get-items",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("상품 식별자")
                        ),
                        requestParameters(
                                List.of(
                                        parameterWithName("page").description("Page 번호"),
                                        parameterWithName("size").description("Page 크기")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data[].itemId").type(JsonFieldType.NUMBER).description("상품 식별자"),
                                        fieldWithPath("data[].itemName").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("data[].price").type(JsonFieldType.NUMBER).description("가격"),
                                        fieldWithPath("data[].soldCnt").type(JsonFieldType.NUMBER).description("판매량"),
                                        fieldWithPath("data[].stockCnt").type(JsonFieldType.NUMBER).description("재고량"),
                                        fieldWithPath("data[].info").type(JsonFieldType.STRING).description("상품 정보"),

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
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN"})
    void patchItemTest() throws Exception {
        Long itemId = 1L;
        Item item = ItemDummy.createItem1();
        ItemPatchDto itemPatchDto = ItemDummy.createPatchDto();
        String content = gson.toJson(itemPatchDto);
        given(itemMapper.itemPatchDtoToItem(Mockito.any(ItemPatchDto.class)))
                .willReturn(item);

        ResultActions actions = mockMvc.perform(
                patch("/api/v1/item/{itemId}", itemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .content(content)
        );

        actions.andExpect(status().isOk())
                .andDo(document("patch-item",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("itemId").description("상품 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("itemName").type(JsonFieldType.STRING).description("상품 이름").optional(),
                                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격").optional(),
                                        fieldWithPath("stockCnt").type(JsonFieldType.NUMBER).description("재고량").optional(),
                                        fieldWithPath("info").type(JsonFieldType.STRING).description("상품 설명").optional()
                                )
                        )
                ));
    }

    @Test
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN"})
    void deleteItemTest() throws Exception {
        Long itemId = 1L;
        doNothing().when(itemService).deleteItem(Mockito.anyLong(), Mockito.anyString());

        ResultActions actions = mockMvc.perform(
                delete("/api/v1/item/{itemId}", itemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        );

        actions.andExpect(status().isNoContent())
                .andDo(document("delete-item",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("itemId").description("상품 식별자")
                        )
                ));
    }
}
