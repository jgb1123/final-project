package com.solo.delivery.authorize;

import com.google.gson.Gson;
import com.solo.delivery.dummy.OrderDummy;
import com.solo.delivery.exception.GlobalExceptionAdvice;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.order.controller.OrderController;
import com.solo.delivery.order.dto.OrderPatchDto;
import com.solo.delivery.order.entity.Order;
import com.solo.delivery.order.mapper.OrderMapper;
import com.solo.delivery.order.service.OrderService;
import com.solo.delivery.security.config.SecurityConfig;
import com.solo.delivery.security.jwt.JwtTokenizer;
import com.solo.delivery.security.utils.CustomAuthorityUtils;
import com.solo.delivery.util.WithAuthMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import(SecurityConfig.class)
@MockBean({JpaMetamodelMappingContext.class, MemberService.class, JwtTokenizer.class, CustomAuthorityUtils.class, GlobalExceptionAdvice.class})
public class OrderAuthorizeTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderMapper orderMapper;

    @Autowired
    private Gson gson;

    @Test
    @DisplayName("OrderController 수정")
    @WithAuthMember(email = "hgd@gmail.com", roles = {"USER"})
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

        actions.andExpect(status().isForbidden());
    }
}
