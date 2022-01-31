package org.jto.dupcheck

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER)
class SecurityConfiguration() : WebSecurityConfigurerAdapter(true) {

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
            .withUser("guest").password("{noop}pass")
            .roles("GUEST")
    }

    override fun configure(http: HttpSecurity) {
        http
            .httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/").hasAnyRole("GUEST")
            .antMatchers(HttpMethod.GET, "/test").hasAnyRole("GUEST")
            .and()
            .cors().and()
            .csrf().disable()
            .formLogin().disable()
    }
}
