package com.solo.delivery.cart;

import com.google.gson.Gson;
import com.solo.delivery.cart.controller.CartController;
import com.solo.delivery.cart.dto.CartPatchDto;
import com.solo.delivery.cart.dto.CartPostDto;
import com.solo.delivery.cart.dto.CartResponseDto;
import com.solo.delivery.cart.entity.Cart;
import com.solo.delivery.cart.mapper.CartMapper;
import com.solo.delivery.cart.service.CartService;
import com.solo.delivery.dummy.CartDummy;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.security.config.SecurityConfig;
import com.solo.delivery.security.jwt.JwtTokenizer;
import com.solo.delivery.security.utils.CustomAuthorityUtils;
import com.solo.delivery.util.WithAuthMember;
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

@WebMvcTest(CartController.class)
@Import(SecurityConfig.class)
@MockBean({JpaMetamodelMappingContext.class, MemberService.class, JwtTokenizer.class, CustomAuthorityUtils.class})
@AutoConfigureRestDocs
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private CartMapper cartMapper;

    @Autowired
    private Gson gson;

    @Test
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN"})
    void postCartTest() throws Exception {
        Long itemId = 1L;
        Cart cart = CartDummy.createCart1();
        CartPostDto cartPostDto = CartDummy.createPostDto();
        String content = gson.toJson(cartPostDto);
        given(cartMapper.cartPostDtoToCart(Mockito.any(CartPostDto.class)))
                .willReturn(new Cart());
        given(cartService.createCart(Mockito.any(Cart.class), Mockito.anyLong(), Mockito.anyString()))
                .willReturn(cart);

        ResultActions actions = mockMvc.perform(
                post("/api/v1/cart/{itemId}", itemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .content(content)
        );

        actions.andExpect(status().isCreated())
                .andDo(document("post-cart",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("itemId").description("상품 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("itemCnt").type(JsonFieldType.NUMBER).description("상품 개수")
                                )
                        )
                ));
    }

    @Test
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN"})
    void getCartsTest() throws Exception {
        Long itemId = 1L;
        int page = 1;
        int size = 1;
        String sort = "cartId,desc";
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", Integer.toString(page));
        queryParams.add("size", Integer.toString(size));
        queryParams.add("sort", sort);
        Cart cart1 = CartDummy.createCart1();
        Cart cart2 = CartDummy.createCart2();
        CartResponseDto cartResponseDto1 = CartDummy.createResponseDto1();
        CartResponseDto cartResponseDto2 = CartDummy.createResponseDto2();
        List<CartResponseDto> responses = List.of(cartResponseDto1, cartResponseDto2);
        Page<Cart> cartPage = new PageImpl<>(List.of(cart1, cart2), PageRequest.of(page - 1, size,
                Sort.by("cartId").descending()), 2);
        given(cartService.findCarts(Mockito.anyString(), Mockito.any(Pageable.class)))
                .willReturn(cartPage);
        given(cartMapper.cartsToCartResponseDtos(Mockito.anyList()))
                .willReturn(responses);

        ResultActions actions = mockMvc.perform(
                get("/api/v1/cart")
                        .params(queryParams)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        );

        actions.andExpect(status().isOk())
                .andDo(document("get-carts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
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
                                        fieldWithPath("data[].cartId").type(JsonFieldType.NUMBER).description("장바구니 식별자"),
                                        fieldWithPath("data[].itemCnt").type(JsonFieldType.NUMBER).description("상품 개수"),
                                        fieldWithPath("data[].itemId").type(JsonFieldType.NUMBER).description("상품 식별자"),
                                        fieldWithPath("data[].itemName").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("data[].itemPrice").type(JsonFieldType.NUMBER).description("상품 가격"),

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
    void patchCartTest() throws Exception {
        Long cartId = 1L;
        Cart cart = CartDummy.createCart1();
        CartPatchDto cartPatchDto = CartDummy.createPatchDto();
        String content = gson.toJson(cartPatchDto);
        given(cartService.updateCart(Mockito.anyLong(), Mockito.any(Cart.class)))
                .willReturn(new Cart());

        ResultActions actions = mockMvc.perform(
                patch("/api/v1/cart/{cartId}", cartId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .content(content)
        );

        actions.andExpect(status().isOk())
                .andDo(document("patch-cart",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("cartId").description("장바구니 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("itemCnt").type(JsonFieldType.NUMBER).description("상품 개수").optional()
                                )
                        )
                ));
    }

    @Test
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN"})
    void deleteCartTest() throws Exception {
        Long cartId = 1L;
        doNothing().when(cartService).deleteCart(Mockito.anyLong());

        ResultActions actions = mockMvc.perform(
                delete("/api/v1/cart/{cartId}", cartId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        );

        actions.andExpect(status().isNoContent())
                .andDo(document("delete-cart",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("cartId").description("장바구니 식별자")
                        )
                ));
    }

    @Test
    @WithAuthMember(email = "hgd@gmail.com", roles = {"ADMIN"})
    void resetCartTest() throws Exception {
        Long cartId = 1L;
        doNothing().when(cartService).resetCart(Mockito.anyString());

        ResultActions actions = mockMvc.perform(
                delete("/api/v1/cart")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        );

        actions.andExpect(status().isNoContent())
                .andDo(document("delete-cart",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}
