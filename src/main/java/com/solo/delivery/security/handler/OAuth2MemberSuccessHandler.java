package com.solo.delivery.security.handler;

import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.security.jwt.JwtTokenizer;
import com.solo.delivery.security.jwt.Tokens;
import com.solo.delivery.security.utils.CustomAuthorityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class OAuth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils customAuthorityUtils;
    private final MemberService memberService;

    public OAuth2MemberSuccessHandler(JwtTokenizer jwtTokenizer,
                                      CustomAuthorityUtils customAuthorityUtils,
                                      MemberService memberService) {
        this.jwtTokenizer = jwtTokenizer;
        this.customAuthorityUtils = customAuthorityUtils;
        this.memberService = memberService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var oAuth2User = (OAuth2User)authentication.getPrincipal();
        String email = String.valueOf(oAuth2User.getAttributes().get("email"));
        String name = String.valueOf(oAuth2User.getAttributes().get("name"));

        List<String> authorities = customAuthorityUtils.createRoles(email);
        if(!existMemberCheck(email)){
            saveMember(email, name);
        }
        Tokens tokens = jwtTokenizer.generateTokens(email, authorities);
        writeTokenResponse(response, tokens.getAccessToken(), tokens.getRefreshToken());
    }

    private boolean existMemberCheck(String email) {
        return memberService.existsEmail(email);
    }

    private void saveMember(String email, String name) {
        Member member = new Member();
        member.changeSignUpInfo(email, name);
        memberService.createMember(member);
    }

    private void writeTokenResponse(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.addHeader("AccessToken", accessToken);
        response.addHeader("RefreshToken", refreshToken);
        response.setContentType("application/json;charset=UTF-8");
        var writer = response.getWriter();
        writer.println("AccessToken : " + accessToken);
        writer.println("RefreshToken : " + refreshToken);
    }
}
