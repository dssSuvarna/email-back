package com.emailmarketingtool.services


import com.emailmarketingtool.configs.JwtService
import com.emailmarketingtool.entities.Sender
import com.emailmarketingtool.enums.Role
import com.emailmarketingtool.errors.BaseError
import com.emailmarketingtool.errors.ErrorMessages
import com.emailmarketingtool.exceptions.NotFoundException
import com.emailmarketingtool.models.requests.CreateSenderRequest
import com.emailmarketingtool.models.requests.UpdateSenderRequest
import com.emailmarketingtool.models.responses.SenderResponse
import com.emailmarketingtool.repositories.AuthUserRepository
import com.emailmarketingtool.repositories.NotificationRepository
import com.emailmarketingtool.repositories.SenderRepository
import com.emailmarketingtool.repositories.UserRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class SenderService @Autowired constructor(
    private val senderRepository: SenderRepository,
    private val userRepository: UserRepository,
    private val authUserRepository: AuthUserRepository,
    private val notificationRepository: NotificationRepository
) {

    fun addSender(token: String, createSenderRequest: CreateSenderRequest): String {
        val authUser = authUserRepository.findByUsername(JwtService.extractUsername(token)) ?: throw UsernameNotFoundException(ErrorMessages.AUTH_USER_NOT_FOUND)
        val loggedInUser = userRepository.findUserByAuthUser(authUser.username)
            ?: throw UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND)
        if (loggedInUser.id != createSenderRequest.userId){
            throw IllegalArgumentException(ErrorMessages.MISS_MATCH_USER)
        }
        val sender = Sender(
            mail = createSenderRequest.mail,
            host = createSenderRequest.host,
            port = createSenderRequest.port,
            signatures = createSenderRequest.signatures,
            userId = loggedInUser.id,
            passKey = createSenderRequest.passKey
        )
        senderRepository.save(sender)
        return "Created"
    }

    fun deleteSender(senderId: Long, token: String): String {
        val notifications =  notificationRepository.findAllBySenderId(senderId)
        notificationRepository.deleteAll(notifications)
        val authUser = authUserRepository.findByUsername(JwtService.extractUsername(token)) ?: throw UsernameNotFoundException(ErrorMessages.AUTH_USER_NOT_FOUND)
        val loggedInUser = userRepository.findUserByAuthUser(authUser.username)
            ?: throw UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND)
        val sender: Sender
        if (authUser.role.name.name == Role.ADMIN.toString()){
            sender =  senderRepository.findById(senderId).orElseThrow {
                NotFoundException(
                    BaseError(
                        HttpStatus.NOT_FOUND,
                        ErrorMessages.SENDER_NOT_FOUND+"$senderId"
                    )
                )
            }
        } else{
            sender = senderRepository.findByUserIdAndId(loggedInUser.id, senderId)  ?: throw NotFoundException(
                BaseError(
                    HttpStatus.NOT_FOUND,
                    ErrorMessages.SENDER_NOT_ASSOCIATED_WITH_USER+"$senderId"
                )
            )
        }
        senderRepository.deleteById(sender.id)
        return "Deleted"
    }

    @Transactional
    fun updateSender(token: String, updateSenderRequest: UpdateSenderRequest): String {
        val authUser = authUserRepository.findByUsername(JwtService.extractUsername(token)) ?: throw UsernameNotFoundException(ErrorMessages.AUTH_USER_NOT_FOUND)
        val loggedInUser = userRepository.findUserByAuthUser(authUser.username)
            ?: throw UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND)
        val sender: Sender
        if (authUser.role.name.name == Role.ADMIN.toString()){
            if (updateSenderRequest.userId != null){
                 userRepository.findById(updateSenderRequest.userId!!)
                    .orElseThrow { EntityNotFoundException(ErrorMessages.USER_NOT_FOUND) }
            }
            sender = senderRepository.findById(updateSenderRequest.senderId).orElseThrow {
                NotFoundException(
                    BaseError(
                        HttpStatus.NOT_FOUND,
                        ErrorMessages.SENDER_NOT_FOUND+"${updateSenderRequest.senderId}"
                    )
                )
            }
            sender.userId = updateSenderRequest.userId ?: sender.userId
        } else {
                if ( updateSenderRequest.userId != null && loggedInUser.id != updateSenderRequest.userId){
                    throw IllegalArgumentException(ErrorMessages.MISS_MATCH_USER)
                }
                sender = senderRepository.findByUserIdAndId(loggedInUser.id, updateSenderRequest.senderId)  ?: throw NotFoundException(
                    BaseError(
                        HttpStatus.NOT_FOUND,
                        ErrorMessages.SENDER_NOT_ASSOCIATED_WITH_USER+"${updateSenderRequest.senderId}"
                    )
                )
        }
        sender.host = updateSenderRequest.host ?: sender.host
        sender.mail = updateSenderRequest.mail ?: sender.mail
        sender.port = updateSenderRequest.port ?: sender.port
        sender.passKey = updateSenderRequest.passKey ?: sender.passKey
        sender.signatures = updateSenderRequest.signatures ?: sender.signatures
        return "Updated"
    }

    fun getSenders(token: String): List<SenderResponse> {
        val authUser = authUserRepository.findByUsername(JwtService.extractUsername(token)) ?: throw UsernameNotFoundException(ErrorMessages.AUTH_USER_NOT_FOUND)
        val loggedInUser = userRepository.findUserByAuthUser(authUser.username)
            ?: throw UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND)

        val sender: List<Sender> = if (authUser.role.name.name == Role.ADMIN.toString()){
            senderRepository.findAll()
        } else{
            senderRepository.findSenderByUserId(loggedInUser.id)
        }
        return sender.map {
            SenderResponse(
                senderId = it.id,
                mail = it.mail,
                host = it.host,
                userId = it.userId,
                signatures = it.signatures,
                port = it.port,
                passKey = it.passKey
            )
        }
    }

}
