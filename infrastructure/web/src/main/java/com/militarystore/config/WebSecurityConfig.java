package com.militarystore.config;

import com.militarystore.port.in.user.UserAuthenticationUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(request -> request
                .requestMatchers("/", "/registration/**", "/error/**", "/auth", "/static/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/products/{productId}", "/products/subcategory-id/{subcategoryId}", "/products/product-name/{productName}").permitAll()
                .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                .anyRequest().authenticated())
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .usernameParameter("login")
                .passwordParameter("password")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .permitAll())
            .exceptionHandling(withDefaults())
            .rememberMe(rememberMe -> rememberMe
                .key("uniqueAndSecret")
                .rememberMeCookieName("remember-me")
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(600));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserAuthenticationUseCase userAuthenticationUseCase) {
        return userAuthenticationUseCase::getUserByLogin;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

        roleHierarchy.setHierarchy("ROLE_SUPER_ADMIN > ROLE_ADMIN > ROLE_USER");

        return roleHierarchy;
    }
}
