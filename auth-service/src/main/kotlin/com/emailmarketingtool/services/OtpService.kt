package com.emailmarketingtool.services

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

@Service
class OtpService {

    private val otps = ConcurrentHashMap<String, Pair<String, Long>>() // <email, <otp, expirationTimeInMillis>>

    fun generateAndStoreOtp(username: String): String {
        val otp = generateOtp()
        val expirationTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)
        otps[username] = otp to expirationTime
        return otp
    }

    fun isOtpValid(email: String, otp: String): Boolean {
        val storedOtpData = otps[email]
        if (storedOtpData != null && System.currentTimeMillis() <= storedOtpData.second) {
            return storedOtpData.first == otp
        }
        return false
    }

    @Scheduled(fixedRate = 60000) // Runs every minute
    fun removeExpiredOtps() {
        val currentTime = System.currentTimeMillis()
        otps.entries.removeIf { entry -> currentTime > entry.value.second }
    }

    fun generateOtp(): String {
        val otpLength = 6
        val random = Random()
        val otp = StringBuilder()

        repeat(otpLength) {
            otp.append(random.nextInt(10))
        }
        return otp.toString()
    }
}
