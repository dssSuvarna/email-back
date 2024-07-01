package com.emailmarketingtool.controller

import com.emailmarketingtool.enums.UserStatus
import com.emailmarketingtool.models.requests.UpdateUserRequest
import com.emailmarketingtool.models.responses.UpdateUserResponse
import com.emailmarketingtool.models.responses.UsersResponse
import com.emailmarketingtool.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController @Autowired constructor(
    val userService: UserService
) {
    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getUserById(@PathVariable("id") userId: Long): ResponseEntity<UsersResponse> {
        val response = userService.getUserById(userId)
        return ResponseEntity.accepted().body(response)
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllUsers(): ResponseEntity<List<UsersResponse>> {
        val response = userService.getAllUsers()
        return ResponseEntity.accepted().body(response)
    }

    @PutMapping("/update")
    @PreAuthorize("hasPermission('hasAccess','user.update')")
    fun updateUser(
        @RequestBody updateUserRequest: UpdateUserRequest,
        @RequestHeader(name = "Authorization") token: String
    ): ResponseEntity<UpdateUserResponse> {
        val updated = userService.updateUser(updateUserRequest, token.substring(7))
        return ResponseEntity(updated, HttpStatus.OK)
    }

    @PutMapping("/update-status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateStatus(@PathVariable("id") userId: Long, @RequestParam status: UserStatus): ResponseEntity<String> {
        return ResponseEntity(userService.updateStatus(userId, status), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteUser(@PathVariable("id") userId: Long): ResponseEntity<String> {
        return ResponseEntity(userService.deleteUser(userId), HttpStatus.OK)
    }

}