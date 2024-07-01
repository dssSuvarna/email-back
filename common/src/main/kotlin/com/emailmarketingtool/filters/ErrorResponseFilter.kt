package com.emailmarketingtool.filters

import com.emailmarketingtool.errors.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class ErrorResponseFilter : Filter {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun doFilter(request: ServletRequest, servletResponse: ServletResponse, chain: FilterChain) {
        val httpResponse = servletResponse as HttpServletResponse

        try {
            chain.doFilter(request, servletResponse)
        } catch (ex: Exception) {
            // Handle the exception and set error response
            httpResponse.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            httpResponse.contentType = "application/json"
            val response = ResponseEntity.internalServerError()
                .body(
                    ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ex.message.orEmpty()
                    )
                )
            httpResponse.writer.write(objectMapper.writeValueAsString(response))
        }
    }
}
