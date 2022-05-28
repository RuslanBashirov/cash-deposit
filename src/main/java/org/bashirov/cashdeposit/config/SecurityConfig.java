package org.bashirov.cashdeposit.config;

import org.bashirov.cashdeposit.config.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.addFilterBefore(jwtFilter, BasicAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers(HttpMethod.PATCH,"/users/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/users/**").authenticated()
                .antMatchers(HttpMethod.POST, "/profiles/create/**").authenticated()
                .antMatchers(HttpMethod.POST, "/phones/create/**").authenticated()
                .antMatchers("/swagger-ui/").permitAll()
                .antMatchers("/users/**").permitAll()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
