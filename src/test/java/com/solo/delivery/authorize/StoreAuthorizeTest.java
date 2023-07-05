package com.solo.delivery.authorize;

import com.google.gson.Gson;
import com.solo.delivery.dummy.StoreDummy;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.security.config.SecurityConfig;
import com.solo.delivery.security.jwt.JwtTokenizer;
import com.solo.delivery.security.utils.CustomAuthorityUtils;
import com.solo.delivery.store.controller.StoreController;
import com.solo.delivery.store.dto.StorePatchDto;
import com.solo.delivery.store.dto.StorePostDto;
import com.solo.delivery.store.entity.Store;
import com.solo.delivery.store.mapper.StoreMapper;
import com.solo.delivery.store.service.StoreService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreController.class)
@Import(SecurityConfig.class)
@MockBean({JpaMetamodelMappingContext.class, MemberService.class, JwtTokenizer.class, CustomAuthorityUtils.class})
public class StoreAuthorizeTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;

    @MockBean
    private StoreMapper storeMapper;

    @Autowired
    private Gson gson;

    @Test
    @DisplayName("StoreController 생성 접근 실패 (USER, SELLER)")
    @WithAuthMember(email = "hgd@gmail.com", roles = {"USER", "SELLER"})
    void postStoreUserAndSellerDenyTest() throws Exception {
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

        actions.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("StoreController 수정 접근 실패 (SELLER)")
    @WithAuthMember(email = "hgd@gmail.com", roles = {"USER"})
    void patchStoreUserDenyTest() throws Exception {
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

        actions.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("StoreController 삭제 접근 실패 (USER, SELLER)")
    @WithAuthMember(email = "hgd@gmail.com", roles = {"USER", "SELLER"})
    void deleteStoreUserAndSellerDenyTest() throws Exception {
        Long storeId = 1L;
        doNothing().when(storeService).deleteStore(Mockito.anyLong());

        ResultActions actions = mockMvc.perform(
                delete("/api/v1/store/{storeId}", storeId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        );

        actions.andExpect(status().isForbidden());
    }
}
