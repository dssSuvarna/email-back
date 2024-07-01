package com.emailmarketingtool.repositories

import com.emailmarketingtool.entities.Template
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TemplateRepository : JpaRepository<Template, Long> {
}