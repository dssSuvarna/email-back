package com.emailmarketingtool.services

import com.emailmarketingtool.entities.Template
import com.emailmarketingtool.models.requests.CreateTemplateRequest
import com.emailmarketingtool.models.requests.UpdateTemplateRequest
import com.emailmarketingtool.repositories.CampaignRepository
import com.emailmarketingtool.repositories.NotificationRepository
import com.emailmarketingtool.repositories.TemplateRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer

@Service
class TemplateService @Autowired constructor(
    private val templateRepository: TemplateRepository,
    private val campaignRepository: CampaignRepository,
    private val freemarkerConfigurer: FreeMarkerConfigurer,
    private val notificationRepository: NotificationRepository

    ) {
    fun createCTemplate(createTemplateRequest: CreateTemplateRequest): String {
        templateRepository.save(
            Template(
                name = createTemplateRequest.name,
                content = createTemplateRequest.content
            )
        )
        return "Created"
    }

    fun updateTemplate(updateTemplateRequest: UpdateTemplateRequest): String {
        val existingTemplate: Template = templateRepository.findById(updateTemplateRequest.templateId)
            .orElseThrow { EntityNotFoundException("Template not found") }

        existingTemplate.content = updateTemplateRequest.content
        templateRepository.save(existingTemplate)
        return "Updated"
    }

    fun deleteTemplate(templateId: Long): String {
        val notifications =  notificationRepository.findAllByTemplateId(templateId)
        notificationRepository.deleteAll(notifications)
        templateRepository.findById(templateId)
            .orElseThrow { EntityNotFoundException("Template not found") }
        templateRepository.deleteById(templateId)
        return "Template deleted"
    }

    fun findById(id: Long): Template {
        return templateRepository.findById(id).orElseThrow { EntityNotFoundException("Template not found") }
    }

    fun getCampaignContacts(campaignId: Long): List<Template> {
        val campaign =
            campaignRepository.findById(campaignId).orElseThrow { EntityNotFoundException("Campaign not found") }
        return campaign.templates
    }

    fun getTemplateWithPlaceHolder(
        payload: Map<String, String?>,
        content: String
    ): String {
        val template = freemarkerConfigurer.configuration.getTemplate(content)
        val templateBody = FreeMarkerTemplateUtils.processTemplateIntoString(
            template, payload
        )
        return templateBody
    }

    fun getAllTemplates(): List<Template> {
        return templateRepository.findAll();
    }

}
