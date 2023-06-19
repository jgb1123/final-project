package com.solo.delivery.authorize;

import com.google.gson.Gson;
import com.solo.delivery.dummy.ItemDummy;
import com.solo.delivery.item.controller.ItemController;
import com.solo.delivery.item.dto.ItemPatchDto;
import com.solo.delivery.item.dto.ItemPostDto;
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

@WebMvcTest(ItemController.class)
@Import(SecurityConfig.class)
@MockBean({JpaMetamodelMappingContext.class, MemberService.class, JwtTokenizer.class, CustomAuthorityUtils.class})
public class itemAuthorizeTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemMapper itemMapper;

    @Autowired
    private Gson gson;

    @Test
    @WithAuthMember(email = "hgd@gmail.com", roles = {"USER"})
    void postItemUserDenyTest() throws Exception {
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

        actions.andExpect(status().isForbidden());
    }

    @Test
    @WithAuthMember(email = "hgd@gmail.com", roles = {"USER"})
    void patchItemUserDenyTest() throws Exception {
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

        actions.andExpect(status().isForbidden());
    }

    @Test
    @WithAuthMember(email = "hgd@gmail.com", roles = {"USER"})
    void deleteItemUserDenyTest() throws Exception {
        Long itemId = 1L;
        doNothing().when(itemService).deleteItem(Mockito.anyLong(), Mockito.anyString());

        ResultActions actions = mockMvc.perform(
                delete("/api/v1/item/{itemId}", itemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        );

        actions.andExpect(status().isForbidden());
    }
}
