package com.emailmarketingtool.services

import com.emailmarketingtool.configs.JwtService
import com.emailmarketingtool.entities.User
import com.emailmarketingtool.enums.Role
import com.emailmarketingtool.enums.UserStatus
import com.emailmarketingtool.errors.ErrorMessages
import com.emailmarketingtool.models.requests.UpdateUserRequest
import com.emailmarketingtool.models.responses.UpdateUserResponse
import com.emailmarketingtool.models.responses.UsersResponse
import com.emailmarketingtool.repositories.AuthUserRepository
import com.emailmarketingtool.repositories.UserRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
    private val authUserRepository: AuthUserRepository
){

    @Transactional
    fun updateUser(updateUserRequest: UpdateUserRequest, token: String): UpdateUserResponse {
        val authUser = authUserRepository.findByUsername(JwtService.extractUsername(token)) ?: throw UsernameNotFoundException(
            ErrorMessages.AUTH_USER_NOT_FOUND)
        val user = userRepository.findUserByAuthUser(authUser.username) ?: throw UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND)

        if (authUser.role.name.name == Role.USER.name && user.id != updateUserRequest.id){
            throw IllegalArgumentException("User id is not matching with logged in user")
        }

        val existingUser: User = userRepository.findById(updateUserRequest.id)
            .orElseThrow { EntityNotFoundException(ErrorMessages.USER_NOT_FOUND) }
        existingUser.firstName = updateUserRequest.firstName ?: existingUser.firstName
        existingUser.lastName = updateUserRequest.lastName ?: existingUser.lastName
        if (updateUserRequest.username != null && !isValidUserName(updateUserRequest.username!!)) {
            throw IllegalArgumentException("Invalid user name format : ${updateUserRequest.username}")
        }
        existingUser.authUser.username = updateUserRequest.username ?: existingUser.authUser.username
        return UpdateUserResponse("Updated")
    }
    fun getUserById(userId: Long): UsersResponse {
        val user: User = userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException(ErrorMessages.USER_NOT_FOUND) }

        return   UsersResponse(
            id = user.id,
            username = user.authUser.username,
            firstName = user.firstName,
            lastName = user.lastName,
            role = user.getUserRole().name,
            status = user.status
        )
    }
    fun getAllUsers(): List<UsersResponse> {
        val usersResponse = userRepository.findAll().filterNot {
            it.getUserRole().name == Role.ADMIN.name
        }.map {
            UsersResponse(
                id = it.id,
                username = it.authUser.username,
                firstName = it.firstName,
                lastName = it.lastName,
                role = it.getUserRole().name,
                status = it.status
            )
        }
        return usersResponse
    }

    @Transactional
    fun updateStatus(id: Long, status: UserStatus): String {
        val user: User = userRepository.findById(id)
            .orElseThrow { EntityNotFoundException(ErrorMessages.USER_NOT_FOUND) }
        if (status == UserStatus.DEACTIVE){
            user.status = status
            return "User deactivated"
        } else if (status == UserStatus.ACTIVE){
            user.status = status
            return "User activated"
        } else {
            throw IllegalArgumentException(ErrorMessages.INVALID_STATUS)
        }
    }

    fun deleteUser(id: Long): String {
        val user: User = userRepository.findById(id)
            .orElseThrow { EntityNotFoundException(ErrorMessages.USER_NOT_FOUND) }
        userRepository.delete(user)
        return "Deleted"
    }

    private fun isValidUserName(userName: String): Boolean {
        val userNameRegex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\$"
        val pattern = Pattern.compile(userNameRegex, Pattern.CASE_INSENSITIVE)
        return pattern.matcher(userName).matches()
    }

}