package com.emailmarketingtool.controller

import com.emailmarketingtool.configs.JwtService
import com.emailmarketingtool.models.requests.CreateSenderRequest
import com.emailmarketingtool.models.requests.UpdateSenderRequest
import com.emailmarketingtool.models.responses.SenderResponse
import com.emailmarketingtool.services.SenderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sender")
class SendersController @Autowired constructor(
    val senderService: SenderService
){

    @PostMapping("/create")
    @PreAuthorize("hasPermission('hasAccess','sender.create')")
    fun addSender(@RequestHeader(name = "Authorization") token: String, @RequestBody createSenderRequest: CreateSenderRequest): ResponseEntity<String> {
        return ResponseEntity(senderService.addSender(token.substring(7), createSenderRequest), HttpStatus.CREATED)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasPermission('hasAccess','sender.delete')")
    fun deleteSender(@RequestHeader(name = "Authorization") token: String, @PathVariable("id") senderId: Long): ResponseEntity<String> {
        return ResponseEntity(senderService.deleteSender(senderId, token.substring(7)), HttpStatus.OK)
    }

    @PutMapping("/update")
    @PreAuthorize("hasPermission('hasAccess','sender.update')")
    fun updateSender(@RequestHeader(name = "Authorization") token: String, @RequestBody updateSenderRequest: UpdateSenderRequest): ResponseEntity<String> {
        return ResponseEntity(senderService.updateSender(token.substring(7), updateSenderRequest), HttpStatus.OK)
    }

    @GetMapping
    @PreAuthorize("hasPermission('hasAccess','sender.view')")
    fun findAllSenders(@RequestHeader(name = "Authorization") token: String): ResponseEntity<List<SenderResponse>> {
        return ResponseEntity(senderService.getSenders(token.substring(7)), HttpStatus.OK)
    }

}