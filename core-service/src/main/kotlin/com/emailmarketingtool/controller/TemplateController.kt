package com.emailmarketingtool.controller

import com.emailmarketingtool.entities.Template
import com.emailmarketingtool.models.requests.CreateTemplateRequest
import com.emailmarketingtool.models.requests.UpdateTemplateRequest
import com.emailmarketingtool.models.responses.ContactResponse
import com.emailmarketingtool.services.TemplateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/template")
class TemplateController @Autowired constructor(
    private val templateService: TemplateService
) {

    @PostMapping("/create")
    @PreAuthorize("hasPermission('hasAccess','template.create')")
    fun createTemplate(@RequestBody createTemplateRequest: CreateTemplateRequest): ResponseEntity<String> {
        return ResponseEntity(templateService.createCTemplate(createTemplateRequest), HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('hasAccess','template.view')")
    fun findTemplateById(@PathVariable id: Long): ResponseEntity<Template> {
        return ResponseEntity(templateService.findById(id), HttpStatus.OK)
    }

    @PutMapping("/update")
    @PreAuthorize("hasPermission('hasAccess','template.update')")
    fun updateTemplate(
        @RequestBody updateTemplateRequest: UpdateTemplateRequest
    ): ResponseEntity<String> {
        return ResponseEntity(templateService.updateTemplate(updateTemplateRequest), HttpStatus.OK)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasPermission('hasAccess','template.delete')")
    fun deleteTemplate(
        @PathVariable id: Long
    ): ResponseEntity<String> {
        return ResponseEntity(templateService.deleteTemplate(id), HttpStatus.OK)
    }


    @GetMapping("/campaign/{id}")
    @PreAuthorize("hasPermission('hasAccess','template.view')")
    fun getCampaignTemplates(@PathVariable("id") campaignId: Long): ResponseEntity<List<Template>> {
        return ResponseEntity(templateService.getCampaignContacts(campaignId),HttpStatus.OK)
    }

    @GetMapping
    @PreAuthorize("hasPermission('hasAccess','template.view')")
    fun getAllTemplates(): ResponseEntity<List<Template>> {
        return ResponseEntity(templateService.getAllTemplates(), HttpStatus.OK)
    }
}
