package com.formssihk.cordaexample.api.payload

import com.formssihk.sample.payload.Loan
import net.corda.core.contracts.StateRef
import java.time.Instant

data class CreatePayload (
    var userPublicKey: String,
    var loadRecords: Map<String, Instant>?
    ) {
}

data class UpdatePayload (
    val userPublicKey: String,
    val loadRecords: List<Loan>
    )