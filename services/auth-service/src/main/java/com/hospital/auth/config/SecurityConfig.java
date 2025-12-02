package com.hospital.auth.config;

import com.hospital.auth.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // DÉSACTIVER la protection CSRF pour les APIs REST
            .csrf(csrf -> csrf.disable())
            // Utiliser des sessions sans état (STATELESS) pour JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Configuration des autorisations
            .authorizeHttpRequests(auth -> auth
                // Les endpoints d'authentification sont PUBLICS
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                // Tous les autres endpoints nécessitent une authentification
                .anyRequest().authenticated()
            )
            // ⚠️ TRÈS IMPORTANT : DÉSACTIVER le formulaire de login par défaut de Spring
            .formLogin(form -> form.disable())
            // ⚠️ DÉSACTIVER l'authentification basic
            .httpBasic(basic -> basic.disable())
            // Ajouter notre filtre JWT personnalisé
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}