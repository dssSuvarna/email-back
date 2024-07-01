package com.emailmarketingtool.services

import com.emailmarketingtool.entities.Contact
import com.emailmarketingtool.models.requests.CreateContactRequest
import com.emailmarketingtool.models.requests.UpdateContactRequest
import com.emailmarketingtool.models.responses.ContactResponse
import com.emailmarketingtool.models.responses.UpdateContactResponse
import com.emailmarketingtool.repositories.CampaignRepository
import com.emailmarketingtool.repositories.ContactRepository
import com.emailmarketingtool.repositories.NotificationRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ContactService @Autowired constructor(
    private val contactRepository: ContactRepository,
    private val campaignRepository: CampaignRepository,
    private val notificationRepository: NotificationRepository
) {
    fun saveContact(createContactRequest: CreateContactRequest): String {
        contactRepository.save(
            Contact(
                name = createContactRequest.name,
                email = createContactRequest.email,
                company = createContactRequest.company,
                group = createContactRequest.group.lowercase(Locale.getDefault())
            )
        )
        return "Created"
    }

    @Transactional
    fun updateContact(updateContactRequest: UpdateContactRequest): UpdateContactResponse {
        val existingContact: Contact = contactRepository.findById(updateContactRequest.contactId)
            .orElseThrow { EntityNotFoundException("Contact not found") }
        existingContact.name = updateContactRequest.name ?: existingContact.name
        existingContact.email = updateContactRequest.email ?: existingContact.email
        existingContact.group = updateContactRequest.group?.lowercase(Locale.getDefault()) ?: existingContact.group
        existingContact.company = updateContactRequest.company ?: existingContact.company
        return UpdateContactResponse("Updated")
    }

    fun getContact(id: Long): ContactResponse {
        val contact: Contact = contactRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Contact not found") }
        return ContactResponse(
            id = contact.id,
            name = contact.name,
            email = contact.email,
            company = contact.company,
            group = contact.group,
            isOpened = contact.isOpened
        )
    }

    fun getAllContacts(): List<ContactResponse> {
        return contactRepository.findAll().map {
            ContactResponse(
                id = it.id,
                name = it.name,
                email = it.email,
                company = it.company,
                group = it.group,
                isOpened = it.isOpened
            )
        }
    }

    fun getCampaignContacts(campaignId: Long): List<ContactResponse> {
        val campaign = campaignRepository.findById(campaignId).orElseThrow{ EntityNotFoundException("Campaign not found")}
        return campaign.contacts.map {
            ContactResponse(
                id = it.id,
                name = it.name,
                email = it.email,
                company = it.company,
                group = it.group,
                isOpened = it.isOpened
            )
        }
    }

    fun deleteContact(id: Long): String {
        val notifications =  notificationRepository.findAllByContactId(id)
        notificationRepository.deleteAll(notifications)
        val contact: Contact = contactRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Contact not found") }
        contactRepository.delete(contact)
        return "Deleted"
    }

    fun getContactsByGroups(groupName: String): List<ContactResponse> {
        val contacts = contactRepository.findByGroup(groupName.lowercase(Locale.getDefault()))
        return contacts.map {
            ContactResponse(
                id = it.id,
                name = it.name,
                email = it.email,
                company = it.company,
                group = it.group,
                isOpened = it.isOpened
            )
        }
    }

    fun getContactGroups(): List<String> {
        val groups: List<String>
        val contacts = contactRepository.findAll()
        groups = contacts
            .filter { it.group.isNotEmpty() }
            .groupBy { it.group }
            .keys.toList()
        return groups.toList()
    }

    @Transactional
    fun uploadContacts(csvFile: MultipartFile, group: String) {
        val contacts = validateCsvFile(csvFile, group)
        val emailToContactMap: Map<String, Contact> = contacts
            .associateBy(Contact::email)
        val existingContacts = contactRepository.findByEmailIn(emailToContactMap.keys.toList())

        if (existingContacts.isNotEmpty()){
            existingContacts.forEach{
                    contact ->
                val currentContact = emailToContactMap[contact.email]
                contact.name = currentContact?.name?:contact.name
                contact.email = currentContact?.email?:contact.email
                contact.company = currentContact?.company?:contact.company
                contact.group = currentContact?.group?:contact.group
            }
        }
        if (existingContacts.isEmpty()){
            contactRepository.saveAll(contacts)
        }
        if (existingContacts.isNotEmpty() && existingContacts.size != contacts.size){
            val nonExistingContacts = contacts.filter { contact ->
                existingContacts.none { existingContact ->
                    existingContact.email == contact.email
                }
            }
            if (nonExistingContacts.isNotEmpty()){
                contactRepository.saveAll(nonExistingContacts)
            }
        }
    }

    private  fun validateCsvFile(csvFile: MultipartFile, group: String): MutableList<Contact> {
        val contacts = mutableListOf<Contact>()

        csvFile.inputStream.bufferedReader().useLines { lines ->
            var headerProcessed = false
            var nameIndex = 0
            var emailIndex = 0
            var companyIndex = 0

            lines.forEachIndexed { index, line ->
                val lineNumber = index + 1 // Line number starts from 1
                val columns = line.split(",").map { it.trim() }

                if (columns.isNotEmpty() && columns.any { it.isNotBlank() }) {
                    // Check if the header has been processed
                    if (!headerProcessed) {
                        if (!columns.containsAll(listOf("email"))) {
                            throw IllegalArgumentException("Header field email is missing in the CSV file.")
                        }
                        nameIndex = columns.indexOf("name")
                        emailIndex = columns.indexOf("email")
                        companyIndex = columns.indexOf("company")
                        headerProcessed = true
                    } else {
                        // Process non-empty lines
                        val name = columns.getOrNull(nameIndex)?.takeIf { it.isNotBlank() }
                        val email = columns.getOrNull(emailIndex)?.trim() ?: ""
                        val company = columns.getOrNull(companyIndex)?.takeIf { it.isNotBlank() }

                        val contactRequest = CreateContactRequest(name = name, email = email, company = company, group = group)
                        val contact = Contact(name = contactRequest.name, email = contactRequest.email, company = contactRequest.company, group = contactRequest.group.lowercase())
                        if (contacts.any { it.email == email }) {
                            throw IllegalArgumentException("Duplicate email found in the CSV file: $email. Error at line $lineNumber")
                        }
                        contacts.add(contact)
                    }
                }
            }
            if (!headerProcessed) {
                throw IllegalArgumentException("The CSV file does not contain the required header fields (name, email, company).")
            }
        }

        if (contacts.isEmpty()) {
            throw IllegalArgumentException("The CSV file must contain at least one contact")
        }
        return contacts
    }
}
