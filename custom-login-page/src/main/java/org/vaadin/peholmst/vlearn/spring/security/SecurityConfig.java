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
                .formLogin().loginPage("/login").failureUrl("/login?error").permitAll() // Remember to call `permitAll()` or you will end up in a redirect loop because `/login` is protected.
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).permitAll() // Remember to call `permitAll` or you won't be redirected to the `logoutSuccessUrl` but to the ordinary login page instead.
                .and()
                // The default request cache is pretty well configured. If you override it with your own, remember
                // to configure the matchers correctly!
                .requestCache()
                .and()
                .csrf().ignoringRequestMatchers(SecurityConfig::isVaadinInternalPostRequest);
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
