package angel_bridge.angel_bridge_server.global.config;

import angel_bridge.angel_bridge_server.global.jwt.CustomLogoutFilter;
import angel_bridge.angel_bridge_server.global.jwt.JWTFilter;
import angel_bridge.angel_bridge_server.global.jwt.JWTUtil;
import angel_bridge.angel_bridge_server.global.oauth2.CustomSuccessHandler;
import angel_bridge.angel_bridge_server.global.oauth2.service.CustomOAuth2UserService;
import angel_bridge.angel_bridge_server.global.repository.MemberRepository;
import angel_bridge.angel_bridge_server.global.repository.RefreshRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final MemberRepository memberRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));
                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8081"));
                        configuration.setAllowedOrigins(Collections.singletonList("http://3.39.14.152:8080"));
                        configuration.setAllowedOrigins(Collections.singletonList("http://3.39.14.152:8081"));
                        configuration.setAllowedMethods(Arrays.asList("*"));


                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        http
                .csrf((auth) -> auth.disable());

        http
                .formLogin((auth) -> auth.disable());

        http
                .httpBasic((auth) -> auth.disable());

        http
                .addFilterAfter(new JWTFilter(jwtUtil,memberRepository), OAuth2LoginAuthenticationFilter.class);

        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                );
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("http://3.39.14.152:8080").permitAll()
                        .requestMatchers("http://3.39.14.152:8081").permitAll()
                        .requestMatchers("http://localhost:3000").permitAll()
                        .requestMatchers("http://localhost:3001").permitAll()

                        // 임시
                        .requestMatchers("/**").permitAll()

                        .requestMatchers("/api/auth/reissue").permitAll()
                        .requestMatchers("/hc", "/env").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated());
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}