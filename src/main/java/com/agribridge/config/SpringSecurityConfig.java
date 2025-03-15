package com.agribridge.config;

import com.agribridge.security.JwtAuthenticationEntryPoint;
import com.agribridge.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SpringSecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter authenticationFilter;

    public SpringSecurityConfig(JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter authenticationFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors(cors->cors.configurationSource(source-> corsConfiguration()))
                .authorizeHttpRequests((authorize)->{
                    authorize.requestMatchers(
                            "/api/v1/auth/**",
                            "/actuator/**",
                            "/v2/api-docs",
                            "/v3/api-docs",
                            "/v3/api-docs/**",
                            "/swagger-resources",
                            "/swagger-resources/**",
                            "/configuration/ui",
                            "/configuration/security",
                            "/swagger-ui/**",
                            "/webjars/**",
                            "/swagger-ui.html").permitAll();
                    try {
                        authorize.anyRequest().authenticated()
                                .and()
                                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).httpBasic(Customizer.withDefaults());
        http.exceptionHandling(exception->
            exception.authenticationEntryPoint(authenticationEntryPoint)
        );

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    //takes care of angular cors issue from spring boot 3
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration cors_config = new CorsConfiguration();
        cors_config.setAllowCredentials(true);
        cors_config.applyPermitDefaultValues();
//        cors_config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        cors_config.setAllowedMethods(List.of("GET","POST","OPTIONS","DELETE"));
        cors_config.setAllowedOriginPatterns(List.of("*"));
        cors_config.setAllowedHeaders(List.of("*"));
        return  cors_config;
    }
}
