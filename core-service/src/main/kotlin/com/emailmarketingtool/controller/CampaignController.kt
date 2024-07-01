package com.emailmarketingtool.controller

import com.emailmarketingtool.entities.Campaign
import com.emailmarketingtool.models.requests.CreateCampaignRequest
import com.emailmarketingtool.models.requests.UpdateCampaignRequest
import com.emailmarketingtool.models.responses.CampaignsResponse
import com.emailmarketingtool.services.CampaignService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/campaigns")
class CampaignController @Autowired constructor(
    private val campaignService: CampaignService
) {

    @PostMapping
    @PreAuthorize("hasPermission('hasAccess','campaign.create')")
    fun createCampaign(
        @RequestPart("createCampaignRequest") createCampaign: String,
        @RequestHeader(name = "Authorization") token: String,
        @RequestPart("file") file: MultipartFile,
        @RequestPart("group") group: String,
    ): ResponseEntity<String> {

        val objectMapper = ObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        val createCampaignRequest: CreateCampaignRequest = objectMapper.readValue(createCampaign, CreateCampaignRequest::class.java)

        if (createCampaignRequest.groupNames?.isEmpty() == true || createCampaignRequest.groupNames == null && file.isEmpty) {
            return ResponseEntity("Please provide CSV file or group name", HttpStatus.BAD_REQUEST)
        }

        if (!file.isEmpty){
            val fileName = file.originalFilename ?: ""
            if (!fileName.endsWith(".csv")) {
                return ResponseEntity("Only CSV files are allowed.", HttpStatus.BAD_REQUEST)
            }
        }
        val createdCampaign = campaignService.createCampaign(createCampaignRequest, token.substring(7), file, group)
        return ResponseEntity(createdCampaign, HttpStatus.CREATED)
    }

    @PutMapping("/update")
    @PreAuthorize("hasPermission('hasAccess','campaign.update')")
    fun updateCampaign(
        @RequestBody updateCampaignRequest: UpdateCampaignRequest
    ): ResponseEntity<Campaign> {
        val updated = campaignService.updateCampaign(updateCampaignRequest)
        return ResponseEntity(updated, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('hasAccess','campaign.view')")
    fun getCampaign(@RequestHeader(name = "Authorization") token: String, @PathVariable("id") campaignId: Long): ResponseEntity<CampaignsResponse> {
        return ResponseEntity(campaignService.getCampaign(token.substring(7), campaignId), HttpStatus.OK)
    }

    @GetMapping
    @PreAuthorize("hasPermission('hasAccess','campaign.view')")
    fun getAllCampaigns(@RequestHeader(name = "Authorization") token: String): ResponseEntity<List<CampaignsResponse>> {
        return ResponseEntity(campaignService.getAllCampaigns(token.substring(7)), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('hasAccess','campaign.delete')")
    fun deleteCampaign(@RequestHeader(name = "Authorization") token: String, @PathVariable id: Long): ResponseEntity<String> {
        return ResponseEntity(campaignService.deleteCampaign(token.substring(7), id), HttpStatus.OK)
    }


    @GetMapping("/open")
    fun trackEmailOpen(
        @RequestParam("campaignId") campaignId: Long,
        @RequestParam("contactId") contactId: Long
    ): ResponseEntity<Void> {
        campaignService.trackEmailOpen(campaignId, contactId)
        System.out.println("just called")
        return ResponseEntity.ok().build()
    }
}
