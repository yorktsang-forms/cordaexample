package com.formssihk.sample.flow

import co.paralleluniverse.fibers.Suspendable
import com.formssihk.sample.contract.IOUContract
import com.formssihk.sample.state.IOUState
import com.formssihk.sample.utils.allParties
import com.formssihk.sample.utils.myIdentity
import net.corda.core.contracts.StateRef
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.loggerFor
import org.slf4j.Logger
import java.time.Instant

@StartableByRPC
class ModifyFlow(val ref: StateRef, val loanRecords: Map<String, Instant>) : IOUFlow(){
    override val log: Logger = loggerFor<CreateFlow>()
    override  val command = IOUContract.Commands.Update()

    @Suspendable
    override fun call(): SignedTransaction {
        val iouState = serviceHub.toStateAndRef<IOUState>(ref)
        val prevLoanRecords = iouState.state.data.loadRecords
        val allLoanRecords = mutableMapOf<String, Instant>().also { it.putAll(prevLoanRecords) }.also { it.putAll(loanRecords) }
        val newIouState = iouState.state.data.copy(loadRecords = allLoanRecords, lastModifiedTime = Instant.now())

        return defaultFlow(newIouState,iouState)
    }
}