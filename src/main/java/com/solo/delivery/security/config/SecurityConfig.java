package com.solo.delivery.security.config;

import com.solo.delivery.member.service.MemberService;
import com.solo.delivery.security.filter.JwtVerificationFilter;
import com.solo.delivery.security.handler.MemberAccessDeniedHandler;
import com.solo.delivery.security.handler.MemberAuthenticationEntryPoint;
import com.solo.delivery.security.handler.OAuth2MemberSuccessHandler;
import com.solo.delivery.security.jwt.JwtTokenizer;
import com.solo.delivery.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils customAuthorityUtils;
    private final MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                .accessDeniedHandler(new MemberAccessDeniedHandler())
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.GET, "/api/v1/member").hasAnyRole("ADMIN", "USER", "SELLER")
                        .antMatchers(HttpMethod.PATCH, "/api/v1/member").hasAnyRole("ADMIN", "USER", "SELLER")
                        .antMatchers(HttpMethod.DELETE, "/api/v1/member").hasAnyRole("ADMIN", "USER", "SELLER")

                        .antMatchers(HttpMethod.POST, "/api/v1/store").hasAnyRole("ADMIN")
                        .antMatchers(HttpMethod.PATCH, "/api/v1/store/**").hasAnyRole("ADMIN", "SELLER")
                        .antMatchers(HttpMethod.DELETE, "/api/v1/store/**").hasAnyRole("ADMIN")

                        .antMatchers(HttpMethod.POST, "/api/v1/item/**").hasAnyRole("ADMIN", "SELLER")
                        .antMatchers(HttpMethod.PATCH, "/api/v1/item/**").hasAnyRole("ADMIN", "SELLER")
                        .antMatchers(HttpMethod.DELETE, "/api/v1/item/**").hasAnyRole("ADMIN", "SELLER")

                        .antMatchers(HttpMethod.POST, "/api/v1/review/**").hasAnyRole("ADMIN", "USER", "SELLER")
                        .antMatchers(HttpMethod.PATCH, "/api/v1/review/**").hasAnyRole("ADMIN", "USER", "SELLER")
                        .antMatchers(HttpMethod.DELETE, "/api/v1/review/**").hasAnyRole("ADMIN", "USER", "SELLER")

                        .antMatchers(HttpMethod.POST, "/api/v1/cart/**").hasAnyRole("ADMIN", "USER", "SELLER")
                        .antMatchers(HttpMethod.GET, "/api/v1/cart**").hasAnyRole("ADMIN", "USER", "SELLER")
                        .antMatchers(HttpMethod.PATCH, "/api/v1/cart/**").hasAnyRole("ADMIN", "USER", "SELLER")
                        .antMatchers(HttpMethod.DELETE, "/api/v1/cart/**").hasAnyRole("ADMIN", "USER", "SELLER")

                        .antMatchers(HttpMethod.POST, "/api/v1/order/**").hasAnyRole("ADMIN", "USER", "SELLER")
                        .antMatchers(HttpMethod.GET, "/api/v1/order/**").hasAnyRole("ADMIN", "USER", "SELLER")
                        .antMatchers(HttpMethod.GET, "/api/v1/order**").hasAnyRole("ADMIN", "USER", "SELLER")
                        .antMatchers(HttpMethod.PATCH, "/api/v1/order/**").hasAnyRole("ADMIN", "USER", "SELLER")
                        .antMatchers(HttpMethod.DELETE, "/api/v1/order/**").hasAnyRole("ADMIN", "USER", "SELLER")
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(new OAuth2MemberSuccessHandler(jwtTokenizer, customAuthorityUtils, memberService))
                );
        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PATCH", "DELETE"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, customAuthorityUtils);

            builder.addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class); // (1)
        }
    }
}
