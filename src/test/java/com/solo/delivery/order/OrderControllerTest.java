package com.solo.delivery.order;


import com.google.gson.Gson;
import com.solo.delivery.dummy.OrderDummy;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.order.controller.OrderController;
import com.solo.delivery.order.dto.OrderPatchDto;
import com.solo.delivery.order.dto.OrderPostDto;
import com.solo.delivery.order.dto.OrderResponseDto;
import com.solo.delivery.order.entity.Order;
import com.solo.delivery.order.mapper.OrderMapper;
import com.solo.delivery.order.service.OrderService;
import com.solo.delivery.security.config.SecurityConfig;
import com.solo.delivery.security.jwt.JwtTokenizer;
import com.solo.delivery.security.utils.CustomAuthorityUtils;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import(SecurityConfig.class)
@MockBean({JpaMetamodelMappingContext.class, MemberService.class, JwtTokenizer.class, CustomAuthorityUtils.class})
@AutoConfigureRestDocs
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderMapper orderMapper;

    @Autowired
    private Gson gson;

    @Test
    void PostOrderTest() throws Exception {
        Order order = OrderDummy.createOrder1();
        OrderPostDto orderPostDto = OrderDummy.createPostDto();
        String content = gson.toJson(orderPostDto);
        given(orderMapper.orderPostDtoToOrder(Mockito.any(OrderPostDto.class)))
                .willReturn(new Order());
        given(orderService.createOrder(Mockito.any(Order.class), Mockito.anyList(), Mockito.anyString()))
                .willReturn(new Order());

        ResultActions actions = mockMvc.perform(
                post("/api/v1/order")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .content(content)
        );

        actions.andExpect(status().isCreated())
                .andDo(document("post-order",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("orderDetails").type(JsonFieldType.ARRAY).description("주문 상세 리스트"),
                                        fieldWithPath("orderDetails.[].itemId").type(JsonFieldType.NUMBER).description("상품 아이디"),
                                        fieldWithPath("orderDetails.[].itemOrderCnt").type(JsonFieldType.NUMBER).description("상품 주문 개수"),
                                        fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("requirement").type(JsonFieldType.STRING).description("요청사항")
                                )
                        )
                ));
    }

    @Test
    void getOrderTest() throws Exception {
        Long orderId = 1L;
        OrderResponseDto orderResponseDto = OrderDummy.createResponseDto1();
        given(orderService.findOrder(Mockito.anyLong()))
                .willReturn(new Order());
        given(orderMapper.orderToOrderResponseDto(Mockito.any(Order.class)))
                .willReturn(orderResponseDto);

        ResultActions actions = mockMvc.perform(
                get("/api/v1/order/{orderId}", orderId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orderId").value(orderResponseDto.getOrderId()))
                .andExpect(jsonPath("$.data.memberId").value(orderResponseDto.getMemberId()))
                .andExpect(jsonPath("$.data.address").value(orderResponseDto.getAddress()))
                .andExpect(jsonPath("$.data.phone").value(orderResponseDto.getPhone()))
                .andExpect(jsonPath("$.data.requirement").value(orderResponseDto.getRequirement()))
                .andDo(document("get-order",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("orderId").description("주문 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.orderId").type(JsonFieldType.NUMBER).description("주문 식별자"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("주문자 식별자"),
                                        fieldWithPath("data.address").type(JsonFieldType.STRING).description("주문자 주소"),
                                        fieldWithPath("data.phone").type(JsonFieldType.STRING).description("주문자 전화번호"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("주문자 이름"),
                                        fieldWithPath("data.requirement").type(JsonFieldType.STRING).description("요청사항"),
                                        fieldWithPath("data.orderPrice").type(JsonFieldType.NUMBER).description("총 주문 금액"),
                                        fieldWithPath("data.orderStatus").type(JsonFieldType.STRING).description("주문 상태"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("주문 생성일"),
                                        fieldWithPath("data.orderDetails.[]").type(JsonFieldType.ARRAY).description("주문 상세 리스트"),
                                        fieldWithPath("data.orderDetails.[].orderDetailId").type(JsonFieldType.NUMBER).description("주문상세 식별자"),
                                        fieldWithPath("data.orderDetails.[].itemOrderCnt").type(JsonFieldType.NUMBER).description("상품 주문 개수"),
                                        fieldWithPath("data.orderDetails.[].itemId").type(JsonFieldType.NUMBER).description("상품 식별자"),
                                        fieldWithPath("data.orderDetails.[].itemName").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("data.orderDetails.[].itemPrice").type(JsonFieldType.NUMBER).description("상품 가격")
                                )
                        )

                ));



    }

    @Test
    void getOrdersTest() throws Exception {
        int page = 1;
        int size = 10;
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", Integer.toString(page));
        queryParams.add("size", Integer.toString(size));
        Order order1 = OrderDummy.createOrder1();
        Order order2 = OrderDummy.createOrder2();
        OrderResponseDto orderResponseDto1 = OrderDummy.createResponseDto1();
        OrderResponseDto orderResponseDto2 = OrderDummy.createResponseDto2();
        List<OrderResponseDto> responses = List.of(orderResponseDto1, orderResponseDto2);
        Page<Order> orderPage = new PageImpl<>(List.of(order1, order2), PageRequest.of(page - 1, size,
                Sort.by("orderId").ascending()), 2);
        given(orderService.findOrders(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(orderPage);
        given(orderMapper.ordersToOrderResponseDtos(Mockito.anyList()))
                .willReturn(responses);

        ResultActions actions = mockMvc.perform(
                get("/api/v1/order")
                        .params(queryParams)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        );

        actions.andExpect(status().isOk())
                .andDo(document("get-orders",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                List.of(
                                        parameterWithName("page").description("Page 번호"),
                                        parameterWithName("size").description("Page 크기")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data[].orderId").type(JsonFieldType.NUMBER).description("주문 식별자"),
                                        fieldWithPath("data[].memberId").type(JsonFieldType.NUMBER).description("주문자 식별자"),
                                        fieldWithPath("data[].address").type(JsonFieldType.STRING).description("주문자 주소"),
                                        fieldWithPath("data[].phone").type(JsonFieldType.STRING).description("주문자 전화번호"),
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("주문자 이름"),
                                        fieldWithPath("data[].requirement").type(JsonFieldType.STRING).description("요청사항"),
                                        fieldWithPath("data[].orderPrice").type(JsonFieldType.NUMBER).description("총 주문 금액"),
                                        fieldWithPath("data[].orderStatus").type(JsonFieldType.STRING).description("주문 상태"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("주문 생성일"),
                                        fieldWithPath("data[].orderDetails.[]").type(JsonFieldType.ARRAY).description("주문 상세 리스트"),
                                        fieldWithPath("data[].orderDetails.[].orderDetailId").type(JsonFieldType.NUMBER).description("주문상세 식별자"),
                                        fieldWithPath("data[].orderDetails.[].itemOrderCnt").type(JsonFieldType.NUMBER).description("상품 주문 개수"),
                                        fieldWithPath("data[].orderDetails.[].itemId").type(JsonFieldType.NUMBER).description("상품 식별자"),
                                        fieldWithPath("data[].orderDetails.[].itemName").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("data[].orderDetails.[].itemPrice").type(JsonFieldType.NUMBER).description("상품 가격"),

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
    void patchOrderTest() throws Exception {
        Long orderId = 1L;
        OrderPatchDto orderPatchDto = OrderDummy.createPatchDto();
        String content = gson.toJson(orderPatchDto);
        given(orderService.updateOrder(Mockito.anyLong(), Mockito.anyString()))
                .willReturn(new Order());

        ResultActions actions = mockMvc.perform(
                patch("/api/v1/order/{orderId}", orderId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .content(content)
        );

        actions.andExpect(status().isOk())
                .andDo(document("patch-order",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("orderId").description("주문 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("orderStatus").type(JsonFieldType.STRING).description("주문 상태")
                                )
                        )
                ));
    }

    @Test
    void deleteOrderTest() throws Exception {
        Long orderId = 1L;

        ResultActions actions = mockMvc.perform(
                delete("/api/v1/order/{orderId}", orderId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        );

        actions.andExpect(status().isOk())
                .andDo(document("delete-order",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("orderId").description("주문 식별자")
                        )
                ));
    }
}
