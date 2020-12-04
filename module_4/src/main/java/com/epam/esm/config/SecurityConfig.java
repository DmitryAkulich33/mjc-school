package com.epam.esm.config;

import com.epam.esm.security.JwtTokenFilter;
import com.epam.esm.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()// отключил csrf и httpBasic
                .csrf().disable()     // потому что они включены по умолчанию если мы унаследуемся от WebSecurityConfigurerAdapter.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Так как я буду авторизировать пользователя по токену, мне не нужно создавать и хранить для него сессию. Поэтому я указал STATELESS.
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // нужно чтобы спринг как-то увидел пользователя в системе. Для этого я указываю ему фильтр, который будет срабатывать при каждом запросе (addFilterBefore). В этом фильтре я буду доставать данные из токена, получать юзера из базы данных и ложить данные пользователя и его роли в Spring Security, чтобы спринг мог дальше выполнять свою работу и определять доступен ли определенный адрес для пользователя.
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
