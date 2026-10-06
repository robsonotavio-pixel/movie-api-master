package com.imdb.movie_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter){
      this.jwtAuthenticationFilter = jwtAuthenticationFilter;
}

 @Bean
 public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
 }
  @Bean 
  public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception {
    http 
       .csrf(csrf -> csrf.disable())
       .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
       .authorizeHttpRequests(auth -> auth
       .requestMatchers("/auth/**").permitAll()
       .requestMatchers("/swagger-ui.html","/swagger-ui/**","/api-docs","/api-docs/**","/v3/api-docs","/v3/api-docs/**").permitAll()
       .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
       .requestMatchers(HttpMethod.GET, "/filmes/**").permitAll()
       .anyRequest().authenticated()
       )
       .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
  
       return http.build();
    }
    }
