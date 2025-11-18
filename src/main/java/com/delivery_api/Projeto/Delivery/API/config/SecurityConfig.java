package com.delivery_api.Projeto.Delivery.API.config;

import com.delivery_api.Projeto.Delivery.API.security.JwtAuthenticationFilter;
import com.delivery_api.Projeto.Delivery.API.security.JwtAuthEntryPoint;
import com.delivery_api.Projeto.Delivery.API.security.JwtAccessDeniedHandler;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final UserDetailsService userDetailsService;
    private final JwtAuthEntryPoint unauthorizedHandler;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    // ROTAS PÚBLICAS
    private static final String[] PUBLIC_POST = {
            "/api/auth/login",
            "/api/auth/register",
            "/clientes"
    };

    private static final String[] PUBLIC_GET = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api-docs/**",
            "/actuator/health",
            "/clientes/buscar/**"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // CORS
                .cors(cors -> cors.configurationSource(req -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("*"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setExposedHeaders(List.of("Authorization")); // NECESSÁRIO para tokens
                    config.setAllowCredentials(false);
                    return config;
                }))

                // CSRF — APIs REST não usam sessão
                .csrf(csrf -> csrf.disable())

                // Sessão sempre stateless
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Tratamento de erros (401 / 403)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(unauthorizedHandler) // 401
                        .accessDeniedHandler(accessDeniedHandler)       // 403
                )

                // Autorização
                .authorizeHttpRequests(auth -> auth
                        // autorizando requisições OPTIONS para CORS preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // públicos POST (login, register, cadastro cliente)
                        .requestMatchers(HttpMethod.POST, PUBLIC_POST).permitAll()

                        // públicos GET (swagger, healthcheck, buscar cliente por nome)
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET).permitAll()

                        // Tudo mais exige autenticação
                        .anyRequest().authenticated()
                )

                // Provider de autenticação
                .authenticationProvider(authenticationProvider())

                // Nosso filtro JWT antes do Default Authentication Filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Provider de autenticação
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Auth Manager para o AuthService
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // BCrypt padrão
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
