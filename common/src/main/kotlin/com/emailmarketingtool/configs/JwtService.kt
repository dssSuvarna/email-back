package com.emailmarketingtool.configs


import com.emailmarketingtool.entities.AuthRole
import com.emailmarketingtool.entities.AuthUser
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {
    companion object {

        private val SECRET_KEY: String = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437"

        fun validateToken(token: String): Boolean {
            if (!isValidToken(token)) {
                throw Exception("token is not valid or expired")
            }
            return true
        }

        fun generateToken(user: AuthUser, expiryTime: Long = 18000000L): String {
            val claims: Map<String, AuthRole> = mapOf("role" to user.role)
            return createToken(claims, user.username, expiryTime)
        }

        private fun createToken(claims: Map<String, Any?>, userName: String, expiryTime: Long): String {
            return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + expiryTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact()
        }

        fun extractUsername(token: String): String {
            return extractClaim(token, Claims::getSubject)
        }

        fun extractRole(token: String): AuthRole {
            val jwtParser: JwtParser = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
            val jwt = jwtParser.parse(token)
            val claims: Claims = jwt.body as Claims
            val mapper = jacksonObjectMapper()
            return mapper.readValue(
                mapper.writeValueAsString(claims["role"]),
                AuthRole::class.java
            )
        }

        private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
            val claims: Claims = extractAllClaims(token)
            return claimsResolver(claims)
        }

        private fun isTokenExpired(token: String): Boolean {
            return extractExpirationDate(token).before(Date())
        }

        private fun extractExpirationDate(token: String): Date {
            return extractClaim(token, Claims::getExpiration)
        }

        private fun isValidToken(token: String): Boolean {
            return !isTokenExpired(token)
        }

        private fun extractAllClaims(token: String): Claims {
            return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).body
        }

        private fun getSignKey(): SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY))
    }
}













