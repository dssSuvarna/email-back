package com.emailmarketingtool.filters

import com.emailmarketingtool.configs.ApiConfig
import com.emailmarketingtool.configs.JwtService
import com.emailmarketingtool.models.RolePermission
import com.emailmarketingtool.models.UserPrincipal
import io.jsonwebtoken.io.IOException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtTokenFilter(val apiConfig: ApiConfig) : OncePerRequestFilter() {

    private val swaggerApis = listOf(
        "/.*/v3/api-docs/swagger-config",
        "/.*/v3/api-docs",
        "/.*/configuration/ui",
        "/.*/swagger-resources/.*",
        "/.*/configuration/security",
        "/.*/swagger-ui/.*",
        "/.*/webjars/.*",
        "/core/campaigns/open"
    )

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {

        val ignorePaths = apiConfig.openApis.plus(swaggerApis)

        if (!ignorePaths.any { request.requestURI.matches(it.toRegex()) }) {
            val header = request.getHeader(HttpHeaders.AUTHORIZATION)
            if (header == "" || !header.startsWith("Bearer ")) {
                chain.doFilter(request, response)
                return
            }

            // Get jwt token and validate
            val token = header.substring(7)
            if (!JwtService.validateToken(token)) {
                chain.doFilter(request, response)
                return
            }

            val authRole = JwtService.extractRole(token)
            val userPrincipal = UserPrincipal(
                username = JwtService.extractUsername(token),
                rolePermissions = RolePermission(
                    role = authRole.name,
                    permissions = authRole.permissions.map { it.name })
            )

            val authentication = UsernamePasswordAuthenticationToken(
                userPrincipal, null,
                listOf(SimpleGrantedAuthority("ROLE_${authRole.name.name}"))
            )
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain.doFilter(request, response)
    }
}