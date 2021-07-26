package com.formssihk.sample.utils

import net.corda.core.crypto.SecureHash
import net.corda.core.identity.Party
import net.corda.core.node.ServiceHub
import net.corda.core.serialization.CordaSerializable

fun ServiceHub.preferredNotary(): Party {
    return networkMapCache.notaryIdentities.first()
}

fun ServiceHub.myIdentity(): Party {
    return myInfo.legalIdentities.first()
}

fun ServiceHub.allParties(): List<Party> {
    val allParties = networkMapCache.allNodes.map { it.legalIdentities.first() }.toMutableList()
    allParties.removeAll(networkMapCache.notaryIdentities)
    return allParties
}

@CordaSerializable
open class FlowResult(val message: String, val id: SecureHash, val ref: String) {
    override fun toString(): String {
        return "Success($message, id=$id)"
    }
}