package com.emailmarketingtool.config

import com.emailmarketingtool.configs.ApiConfig
import com.emailmarketingtool.configs.CustomPermissionEvaluator
import com.emailmarketingtool.filters.JwtTokenFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
class AuthConfig @Autowired constructor(
    val customUserDetailsService: CustomUserDetailsService,
) {

    @Autowired
    lateinit var apiConfig: ApiConfig

    @Autowired
    lateinit var permissionEvaluator: CustomPermissionEvaluator

    @Bean
    fun userDetailsService(): UserDetailsService {
        return customUserDetailsService
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.addFilterBefore(JwtTokenFilter(apiConfig), UsernamePasswordAuthenticationFilter::class.java)
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/**").permitAll()
                .and()
                .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsService())
        authenticationProvider.setPasswordEncoder(passwordEncoder())
        return authenticationProvider
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    @Bean
    fun methodSecurityExpressionHandler(): MethodSecurityExpressionHandler? {
        val handler = DefaultMethodSecurityExpressionHandler()
        handler.setPermissionEvaluator(permissionEvaluator)
        return handler
    }
}
