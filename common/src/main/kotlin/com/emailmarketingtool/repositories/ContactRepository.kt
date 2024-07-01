package com.emailmarketingtool.repositories

import com.emailmarketingtool.entities.Contact
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContactRepository : JpaRepository<Contact, Long> {

    fun findByEmailIn(emails: List<String>): MutableList<Contact>

    fun findByGroup(group: String): List<Contact>

    fun findByGroupIn(group: List<String>): List<Contact>
}