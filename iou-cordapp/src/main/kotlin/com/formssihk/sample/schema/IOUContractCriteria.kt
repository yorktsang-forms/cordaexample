package com.formssihk.sample.schema

import net.corda.core.node.services.Vault
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.node.services.vault.builder

fun getUserPublicKeyEqualCriteria(value: String, status: Vault.StateStatus = Vault.StateStatus.UNCONSUMED ): QueryCriteria {
    val expr = builder { IOUSchemaV1.PersistentIOU::userPublicKey.equal(value) }
    return QueryCriteria.VaultCustomQueryCriteria(expr, status)
}