package com.emailmarketingtool.controller

import com.emailmarketingtool.configs.JwtService
import com.emailmarketingtool.errors.InvalidCredentials
import com.emailmarketingtool.errors.InvalidOtp
import com.emailmarketingtool.errors.InvalidToken
import com.emailmarketingtool.exceptions.BadCredentialsException
import com.emailmarketingtool.exceptions.InvalidOtpException
import com.emailmarketingtool.exceptions.InvalidTokenException
import com.emailmarketingtool.models.requests.*
import com.emailmarketingtool.models.responses.*
import com.emailmarketingtool.services.AuthService
import com.emailmarketingtool.services.OtpService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping
@Validated
class AuthController @Autowired constructor(
    val authService: AuthService,
    val otpService: OtpService
) {

    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    fun createUser(@Valid @RequestBody createUserRequest: CreateUserRequest): ResponseEntity<CreateUserResponse> {
        try {
            val response = authService.signUp(createUserRequest)
            return ResponseEntity.accepted().body(CreateUserResponse(response))
        } catch (e: Exception) {
            throw e
        }
    }
    @PostMapping("/login")
    fun getToken(@Valid @RequestBody authRequest: AuthRequest): ResponseEntity<LoginResponse> {
        try {
            authService.isAuthenticated(authRequest)
            return ResponseEntity.accepted().body(
                LoginResponse(
                    authService.generateToken(authRequest.username)
                )
            )
        } catch (e: Exception) {
            throw BadCredentialsException(InvalidCredentials())
        }
    }

    @GetMapping("/validate")
    fun validateToken(@RequestParam("token") token: String): ResponseEntity<Response> {
        return try {
            authService.validateToken(token)
            ResponseEntity.accepted().body(Response("Valid token"))
        } catch (e: Exception) {
            ResponseEntity.accepted().body(Response("Invalid token"))
        }
    }

    @GetMapping("/role")
    fun getUserRolePermissionsFromToken(
        @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<UserRolesPermissionsResponse> {
        return try {
            ResponseEntity.ok().body(authService.getUserRolesPermissionFromToken(token.substring(7)))
        } catch (e: Exception) {
            throw InvalidTokenException(InvalidToken())
        }
    }


    @PostMapping("/verify-otp")
    fun verifyOtp(@RequestBody verificationRequest: VerificationRequest): ResponseEntity<OtpVerificationResponse> {
        val username = verificationRequest.username
        val userEnteredOtp = verificationRequest.otp
        return if (otpService.isOtpValid(username, userEnteredOtp)) {
            val authUser = authService.findByUsername(username)
            val token = JwtService.generateToken(authUser, expiryTime = 600000L)
            ResponseEntity.accepted().body(
                OtpVerificationResponse(
                    response = "Valid otp",
                    token = token
                )
            )
        } else {
            throw InvalidOtpException(InvalidOtp())
        }
    }

    @PutMapping("/reset-password")
    @PreAuthorize("hasPermission('hasAccess','user.update.password')")
    fun resetPassword(
        @Valid @RequestBody resetPasswordRequest: ResetPasswordRequest,
        @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<Response> {
        authService.resetPassword(resetPasswordRequest, token.substring(7))
        return ResponseEntity.accepted().body(Response("Password Reset Successfully"))
    }

    @GetMapping("/get-user")
    fun getCurrentUser( @RequestHeader(name = "Authorization") token: String): ResponseEntity<UsersResponse> {
        val response = authService.getCurrentUser(token.substring(7))
        return ResponseEntity.accepted().body(response)
    }

    @PutMapping("/update-admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateAdmin(
        @RequestBody updateUserRequest: UpdateUserRequest
    ): ResponseEntity<UpdateUserResponse> {
        val updated = authService.updateAdmin(updateUserRequest)
        return ResponseEntity(updated, HttpStatus.OK)
    }
}
