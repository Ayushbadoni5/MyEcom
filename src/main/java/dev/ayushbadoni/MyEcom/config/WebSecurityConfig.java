package dev.ayushbadoni.MyEcom.config;


import dev.ayushbadoni.MyEcom.entities.RESTAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;


    @Autowired
    private JWTTokenHelper jwtTokenHelper;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize)-> authorize
                .requestMatchers("/v3/api-docs/**","swagger-ui.html","swagger-ui/**","/swagger-resources/**").permitAll()
                        .requestMatchers("/api/auth/register", "/api/auth/verify","/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/products/**","/api/category/**","/api/cart/**").permitAll()
                        .requestMatchers("/api/auth/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/auth/user/**").hasAuthority("USER")
                        .requestMatchers( "/api/order/**").authenticated()
                        .requestMatchers("/oauth2/success").permitAll()
                        .anyRequest().authenticated())

                .oauth2Login((oauth2login)->oauth2login.defaultSuccessUrl("/oauth2/success"))
                .exceptionHandling(
                        (exception) -> exception
                                .authenticationEntryPoint(new RESTAuthenticationEntryPoint()).accessDeniedHandler(((request, response, accessDeniedException) -> {
                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                    response.getWriter().write("403 Forbidden - Access Denied");
                                })))
                .addFilterBefore(new JWTAuthenticationFilter(jwtTokenHelper,userDetailsService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider daoAuthenticationProvider  = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(daoAuthenticationProvider);

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
