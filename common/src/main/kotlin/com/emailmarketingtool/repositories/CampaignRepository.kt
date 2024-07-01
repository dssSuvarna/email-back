package com.emailmarketingtool.repositories

import com.emailmarketingtool.entities.Campaign
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CampaignRepository : JpaRepository<Campaign, Long> {

    fun findAllByCreatedBy(createdBy: Long): List<Campaign>

    @Query(
        value = """
            SELECT * FROM campaign c 
            WHERE  c.id = :campaignId 
            AND JSON_CONTAINS(c.contacts, JSON_OBJECT('id', :contactId))
        """,
        nativeQuery = true
    )
    fun findByIdAndContactId(
        @Param("campaignId") campaignId: Long,
        @Param("contactId") contactId: Long
    ): List<Campaign>
}