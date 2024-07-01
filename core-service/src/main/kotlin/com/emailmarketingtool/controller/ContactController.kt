package com.emailmarketingtool.controller

import com.emailmarketingtool.models.requests.CreateContactRequest
import com.emailmarketingtool.models.requests.UpdateContactRequest
import com.emailmarketingtool.models.responses.ContactResponse
import com.emailmarketingtool.models.responses.CreateContactResponse
import com.emailmarketingtool.models.responses.UpdateContactResponse
import com.emailmarketingtool.services.ContactService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/contacts")
class ContactController @Autowired constructor(
    val contactService: ContactService
) {

    @PostMapping
    @PreAuthorize("hasPermission('hasAccess','contact.create')")
    fun createContact(@RequestBody createContactRequest: CreateContactRequest): ResponseEntity<CreateContactResponse> {
        val createdContact = contactService.saveContact(createContactRequest)
        return ResponseEntity(CreateContactResponse(createdContact), HttpStatus.CREATED)
    }

    @PutMapping("/update")
    @PreAuthorize("hasPermission('hasAccess','contact.update')")
    fun updateContact(
        @RequestBody updateContactRequest: UpdateContactRequest
    ): ResponseEntity<UpdateContactResponse> {
        val updated = contactService.updateContact(updateContactRequest)
        return ResponseEntity(updated, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('hasAccess','contact.view')")
    fun getContact(@PathVariable id: Long): ResponseEntity<ContactResponse> {
        return ResponseEntity(contactService.getContact(id), HttpStatus.OK)
    }

    @GetMapping
    @PreAuthorize("hasPermission('hasAccess','contact.view')")
    fun getAllContacts(): ResponseEntity<List<ContactResponse>> {
        return ResponseEntity(contactService.getAllContacts(), HttpStatus.OK)
    }

    @GetMapping("/campaign/{id}")
    @PreAuthorize("hasPermission('hasAccess','contact.view')")
    fun getCampaignContacts(@PathVariable("id") campaignId: Long): ResponseEntity<List<ContactResponse>> {
        return ResponseEntity(contactService.getCampaignContacts(campaignId),HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('hasAccess','contact.delete')")
    fun deleteContact(@PathVariable id: Long): ResponseEntity<String> {
        return ResponseEntity(contactService.deleteContact(id), HttpStatus.OK)
    }

    @GetMapping("/group/{groupName}")
    @PreAuthorize("hasPermission('hasAccess','contact.view')")
    fun getContactsByGroups(@PathVariable groupName: String): ResponseEntity<List<ContactResponse>> {
        return ResponseEntity(contactService.getContactsByGroups(groupName), HttpStatus.OK)
    }

    @GetMapping("/groups")
    @PreAuthorize("hasPermission('hasAccess','contact.group.view')")
    fun getContactGroups(): ResponseEntity<List<String>> {
        return ResponseEntity(contactService.getContactGroups(), HttpStatus.OK)
    }

    @PostMapping("/upload")
    @PreAuthorize("hasPermission('hasAccess','contact.upload')")
    fun uploadContacts(@RequestParam("file", required = true) file: MultipartFile, @RequestParam("group", required = true) group: String): ResponseEntity<String> {
        if (file.isEmpty) {
            return ResponseEntity("Please upload a CSV file.", HttpStatus.BAD_REQUEST)
        }

        val lines = file.inputStream.bufferedReader().readLines()
        if (lines.isEmpty()) {
            return ResponseEntity("File is empty", HttpStatus.BAD_REQUEST)
        }

        val fileName = file.originalFilename ?: ""
        if (!fileName.endsWith(".csv")) {
            return ResponseEntity("Only CSV files are allowed.", HttpStatus.BAD_REQUEST)
        }
        try {
            contactService.uploadContacts(file, group)
            return ResponseEntity("Contacts uploaded successfully.", HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity("Failed to upload contacts: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}