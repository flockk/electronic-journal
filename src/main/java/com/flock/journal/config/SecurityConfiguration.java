package com.flock.journal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import static com.flock.journal.model.Permission.ADMIN_CREATE;
import static com.flock.journal.model.Permission.ADMIN_DELETE;
import static com.flock.journal.model.Permission.ADMIN_READ;
import static com.flock.journal.model.Permission.ADMIN_UPDATE;

import static com.flock.journal.model.Permission.STUDENT_CREATE;
import static com.flock.journal.model.Permission.STUDENT_DELETE;
import static com.flock.journal.model.Permission.STUDENT_READ;
import static com.flock.journal.model.Permission.STUDENT_UPDATE;

import static com.flock.journal.model.Role.ADMIN;
import static com.flock.journal.model.Role.STUDENT;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration implements WebMvcConfigurer {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;
  private static final String STUDENT_API_PATTERN = "/api/v1/student/**";

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**")
        .addResourceLocations("classpath:/static/");
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf()
        .disable()
        .authorizeHttpRequests()
        .requestMatchers(
            "/css/**",
            "/js/**",
            "/static/**",
            "/register",
            "/login",
            "/logout",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
        )
        .permitAll()

        .requestMatchers("/**")
        .permitAll()

        .requestMatchers(STUDENT_API_PATTERN).hasAnyRole(ADMIN.name(), STUDENT.name())

        .requestMatchers(GET, STUDENT_API_PATTERN)
        .hasAnyAuthority(ADMIN_READ.name(), STUDENT_READ.name())
        .requestMatchers(POST, STUDENT_API_PATTERN)
        .hasAnyAuthority(ADMIN_CREATE.name(), STUDENT_CREATE.name())
        .requestMatchers(PUT, STUDENT_API_PATTERN)
        .hasAnyAuthority(ADMIN_UPDATE.name(), STUDENT_UPDATE.name())
        .requestMatchers(DELETE, STUDENT_API_PATTERN)
        .hasAnyAuthority(ADMIN_DELETE.name(), STUDENT_DELETE.name())

        .anyRequest()
        .authenticated()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .logout()
        .logoutUrl("/logout")
        .addLogoutHandler(logoutHandler)
        .logoutSuccessHandler(
            (request, response, authentication) -> SecurityContextHolder.clearContext()
        )
    ;
    return http.build();
  }
}
