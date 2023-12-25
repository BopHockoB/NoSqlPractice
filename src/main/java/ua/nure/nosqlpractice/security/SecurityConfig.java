package ua.nure.nosqlpractice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ua.nure.nosqlpractice.security.userDetails.UserDetailsServiceImp;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    public static final String[] ENDPOINTS_WHITELIST = {

            "/",
            "/login",
            "/error",
            "/registration/**"
    };
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String DEFAULT_SUCCESS_URL = "/users";
    public static final String USERNAME = "email";
    public static final String PASSWORD = "password";
    private final UserDetailsServiceImp userDetailsService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return true;
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(ENDPOINTS_WHITELIST)
                .permitAll()
                .requestMatchers("/**")
                .authenticated()
                .and()
                .formLogin()
                .loginPage(LOGIN_URL)
                .usernameParameter(USERNAME)
                .passwordParameter(PASSWORD)
                .defaultSuccessUrl(DEFAULT_SUCCESS_URL)
                .permitAll().and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URL))
                .logoutSuccessUrl("/login")
                .and()
                .build();
    }
}