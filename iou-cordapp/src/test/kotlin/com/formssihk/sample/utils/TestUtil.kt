package com.formssihk.sample.utils

import net.corda.core.concurrent.CordaFuture
import net.corda.core.utilities.getOrThrow
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*

object TestUtil {
    fun <T> runFlow(tn: TestParties, future: CordaFuture<T>): T {
        tn.net.runNetwork()
        return future.getOrThrow()
    }

    fun createInstant(timestamp: String): Instant {
        return ZonedDateTime.parse(timestamp).toInstant()
    }

    fun getRandomString(maxLength: Int): String {
        val alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        val n = alphabet.length

        val random = Random() // Or SecureRandom
        val sb = StringBuilder()
        for (i in 0 until maxLength) {
            sb.append(alphabet.get(random.nextInt(n)))
        }
        return sb.toString()
    }
}