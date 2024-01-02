package com.logistic.config;

import com.logistic.security.jwt.AuthTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // this is a configuration class
@EnableMethodSecurity(prePostEnabled = true) // Method base security , hasRole issue
public class SecurityConfig {

    // --AIM-- Set PasswordEncoder, AuthenticationProvider, AuthenticationManager, AuthTokenFilter JwtUtils


    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests().requestMatchers (HttpMethod.OPTIONS, "/**").permitAll() // CORS
                .and()
                .authorizeHttpRequests()
                .requestMatchers (
                        "/login"
                        , "/register"
                        , "/files/download/**"
                        , "/files/display/**"
                        , "/car/visitors/**"
                        , "/contactmessage/visitors"
                        , "/actuator/info"
                        , "/actuator/health"
                ).permitAll()
                .anyRequest().authenticated();


        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // -- CORS Settings
    @Bean
    public WebMvcConfigurer corsConfigure() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*") // we can also define like this : http://127.0.0.1/8080
                        .allowedHeaders("*")
                        .allowedMethods("*");
            }
        };
    }

    // -- For Swagger Documentation
    private static final String AUTH_WHITE_LIST[] = {
            "/v3/api-docs/**", // swagger
            "swagger-ui.html", // swagger
            "/swagger-ui/**", // swagger
            "/",
            "index.html",
            "/images/**",
            "/css/**",
            "/js/**"
    };

    // Permission for above static list
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        WebSecurityCustomizer customizer = new WebSecurityCustomizer() {
            @Override
            public void customize(WebSecurity web) {
                web.ignoring().requestMatchers (AUTH_WHITE_LIST);
            }
        };
        return customizer;
    }


    // -- Set Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    // -- Provider (Ceo)
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // -- Manager (Boss)
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider())
                .build();
    }

    // -- AuthTokenFilter (create JWT Token and validate JWT Token)

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter(null,null);
    }


}