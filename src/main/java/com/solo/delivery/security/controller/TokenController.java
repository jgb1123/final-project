package com.solo.delivery.security.controller;

import com.solo.delivery.member.entity.Member;
import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.security.jwt.JwtTokenizer;
import com.solo.delivery.security.jwt.Tokens;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final JwtTokenizer jwtTokenizer;
    private final MemberService memberService;
    @GetMapping("/api/v1/token/refresh")
    public String refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("RefreshToken");
        String email = jwtTokenizer.getClaims(token, jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey()))
                .getBody()
                .getSubject();
        Member member = memberService.findVerifiedMember(email);
        List<String> roles = member.getRoles();
        Tokens tokens = jwtTokenizer.generateTokens(email, roles);
        response.addHeader("AccessToken", tokens.getAccessToken());
        response.addHeader("RefreshToken", tokens.getRefreshToken());
        return tokens.toString();
    }
}
