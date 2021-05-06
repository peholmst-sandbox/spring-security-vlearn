package org.vaadin.peholmst.vlearn.spring.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().anyRequest().fullyAuthenticated()
                .and()
                .formLogin()
                .and()
                // If we enable CSRF, the logout operation will require a POST request with the correct CSRF token.
                // This is a bit difficult to do from Vaadin, so we just change it to an ordinary GET method:
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .and()
                // Instead of disabling CSRF completely, we can just disable it for Vaadin POST requests.
                .csrf().ignoringRequestMatchers(SecurityConfig::isVaadinInternalPostRequest)
                .and()
                .requestCache().disable();
    }

    private static boolean isVaadinInternalPostRequest(HttpServletRequest request) {
        return request.getMethod().equals("POST") && request.getParameter("v-r") != null;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser("joecool").password(encoder.encode("p")).roles("FOO");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/VAADIN/**");
    }
}
