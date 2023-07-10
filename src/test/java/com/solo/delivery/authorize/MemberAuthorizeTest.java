package com.solo.delivery.authorize;

import com.solo.delivery.mail.service.MailService;
import com.solo.delivery.member.controller.MemberController;
import com.solo.delivery.member.mapper.MemberMapper;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.security.config.SecurityConfig;
import com.solo.delivery.security.jwt.JwtTokenizer;
import com.solo.delivery.security.utils.CustomAuthorityUtils;
import com.solo.delivery.util.WithAuthMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@Import(SecurityConfig.class)
@MockBean({JpaMetamodelMappingContext.class, MemberService.class, JwtTokenizer.class, CustomAuthorityUtils.class, MemberMapper.class, MailService.class})
public class MemberAuthorizeTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("MemberController 권한 부여 접근 실패 (USER, SELLER)")
    @WithAuthMember(email = "hgd@gmail.com", roles = {"USER, SELLER"})
    void authorizeRoleTest() throws Exception {
        Long memberId = 2L;
        String role = "SELLER";
        ResultActions actions = mockMvc.perform(
                patch("/api/v1/member/{memberId}/role/{role}", memberId, role)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        );

        actions
                .andExpect(status().isForbidden());
    }
}
