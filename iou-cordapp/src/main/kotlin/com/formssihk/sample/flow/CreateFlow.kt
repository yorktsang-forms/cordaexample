package com.formssihk.sample.flow

import co.paralleluniverse.fibers.Suspendable
import com.formssihk.sample.contract.IOUContract
import com.formssihk.sample.state.IOUState
import com.formssihk.sample.utils.allParties
import com.formssihk.sample.utils.myIdentity
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.loggerFor
import org.slf4j.Logger
import java.time.Instant

@StartableByRPC
class CreateFlow(val userPublicKey: String, val creationTime: Instant, val loanRecords: Map<String, Instant>) : IOUFlow(){
    override val log: Logger = loggerFor<CreateFlow>()
    override  val command = IOUContract.Commands.Create()

    @Suspendable
    override fun call(): SignedTransaction {
        val newState = IOUState(
            userPublicKey = userPublicKey,
            loadRecords = loanRecords,
            creationTime = creationTime,
            lastModifiedTime = Instant.now(),
            parties = serviceHub.allParties()
        )

        return defaultFlow(newState,null)
    }
}