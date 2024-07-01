package com.emailmarketingtool.repositories

import com.emailmarketingtool.entities.Sender
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SenderRepository : JpaRepository<Sender, Long>{

    fun findSenderByUserId(userId: Long): List<Sender>

    fun findByUserIdAndId(userId: Long, id: Long): Sender?


}