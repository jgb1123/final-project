package com.solo.delivery.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WithAuthMemberSecurityContextFactory implements WithSecurityContextFactory<WithAuthMember> {
    @Override
    public SecurityContext createSecurityContext(WithAuthMember annotation) {
        String email = annotation.email();
        List<String> roles = Arrays.stream(annotation.roles()).collect(Collectors.toList());
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
//        Member member = new Member(1L, "홍길동", "hgd@gmail.com", "010-1234-5678", "hgd123", List.of("ADMIN", "USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        return context;
    }
}
