package com.formssihk.sample.payload

import java.security.MessageDigest
import java.time.Instant

data class Loan2 (
    val borrower: String,
    val lender: String,
    val principal: Double,
    val startDay: Instant
) {
    fun sha256(): String {
        val bytes = this.toString().toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}