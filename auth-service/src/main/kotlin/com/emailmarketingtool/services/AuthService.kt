package com.emailmarketingtool.services

import com.emailmarketingtool.configs.JwtService
import com.emailmarketingtool.entities.AuthUser
import com.emailmarketingtool.entities.User
import com.emailmarketingtool.enums.Role
import com.emailmarketingtool.enums.UserStatus
import com.emailmarketingtool.errors.BaseError
import com.emailmarketingtool.errors.ErrorMessages
import com.emailmarketingtool.errors.ErrorMessages.Companion.AUTH_USER_NOT_FOUND
import com.emailmarketingtool.errors.ErrorMessages.Companion.USER_NOT_FOUND
import com.emailmarketingtool.errors.MisMatchPassword
import com.emailmarketingtool.errors.UserNotFound
import com.emailmarketingtool.exceptions.InvalidPasswordException
import com.emailmarketingtool.exceptions.NotAllowedException
import com.emailmarketingtool.exceptions.NotFoundException
import com.emailmarketingtool.models.RolePermission
import com.emailmarketingtool.models.requests.*
import com.emailmarketingtool.models.responses.UpdateUserResponse
import com.emailmarketingtool.models.responses.UserRolesPermissionsResponse
import com.emailmarketingtool.models.responses.UsersResponse
import com.emailmarketingtool.repositories.AuthRoleRepository
import com.emailmarketingtool.repositories.AuthUserRepository
import com.emailmarketingtool.repositories.UserRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService @Autowired constructor(
    val authUserRepository: AuthUserRepository,
    val authenticationManager: AuthenticationManager,
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val authRoleRepository: AuthRoleRepository
) {

    fun signUp(singUpUserRequest: CreateUserRequest): String {
        val isUserAlreadyRegistered = authUserRepository.findByUsername(singUpUserRequest.username)
        if (isUserAlreadyRegistered != null) {
            throw NotAllowedException(BaseError(HttpStatus.CONFLICT, ErrorMessages.AUTH_USER_ALREADY_REGISTERED))
        }
        userRepository.save(
            User(
                firstName = singUpUserRequest.firstName,
                lastName = singUpUserRequest.lastName,
                authUser = AuthUser(
                    username = singUpUserRequest.username,
                    password = passwordEncoder.encode(singUpUserRequest.password),
                    role = authRoleRepository.getAuthRoleByName(singUpUserRequest.role)!!
                )
            )
        )
        return "Registered"
    }

    fun generateToken(username: String): String {
        val user = authUserRepository.findByUsername(username) ?: throw UsernameNotFoundException(AUTH_USER_NOT_FOUND)
        return JwtService.generateToken(user)
    }

    fun validateToken(token: String): AuthUser  {
        try {
            val username = JwtService.extractUsername(token)
            val authUser = authUserRepository.findByUsername(username) ?: throw UsernameNotFoundException(AUTH_USER_NOT_FOUND)
            JwtService.validateToken(token)
            return  authUser
        } catch (e: Exception) {
            throw Exception("Invalid token")
        }

    }

    fun getAuthentication(authRequest: AuthRequest): Authentication =
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.username,
                authRequest.password
            )
        )

    fun isAuthenticated(authRequest: AuthRequest): Boolean = getAuthentication(authRequest).isAuthenticated

    fun getUserRolesPermissionFromToken(token: String): UserRolesPermissionsResponse {
        val user = authUserRepository
            .findByUsername(JwtService.extractUsername(token)) ?: throw NotFoundException(UserNotFound())
        val authRole = JwtService.extractRole(token)
        return UserRolesPermissionsResponse(
            userId = user.id,
            RolePermission(role = authRole.name, permissions = authRole.permissions.map { it.name })
        )
    }

    @Transactional
    fun resetPassword(resetPasswordRequest: ResetPasswordRequest, token: String) {
        if (resetPasswordRequest.newPassword != resetPasswordRequest.confirmPassword) {
            throw InvalidPasswordException(MisMatchPassword())
        }
        val username = JwtService.extractUsername(token)
        val authUser = findByUsername(username)
        authUser.password = passwordEncoder.encode(resetPasswordRequest.newPassword)
    }

    fun findByUsername(username: String): AuthUser {
        return authUserRepository.findByUsername(username) ?: throw UsernameNotFoundException(AUTH_USER_NOT_FOUND)
    }

    fun getCurrentUser(token: String): UsersResponse {
        val authUser = validateToken(token)
        val user = userRepository.findUserByAuthUser(authUser.username)
            ?: throw UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND)
        val status = user.status ?: UserStatus.ACTIVE
        return  UsersResponse(
            id = user.id,
            username = authUser.username,
            firstName = user.firstName ?: "",
            lastName = user.lastName ?: "",
            role = authUser.role.name.name,
            status = status
        )
    }

    @Transactional
    fun updateAdmin(updateUserRequest: UpdateUserRequest): UpdateUserResponse {
        val user = userRepository.findById(updateUserRequest.id) .orElseThrow { EntityNotFoundException(USER_NOT_FOUND) }
        user.authUser.username = updateUserRequest.username ?: user.authUser.username
        user.firstName = updateUserRequest.firstName ?: user.firstName
        user.lastName = updateUserRequest.lastName ?: user.lastName
        return UpdateUserResponse("Updated")
    }

}










