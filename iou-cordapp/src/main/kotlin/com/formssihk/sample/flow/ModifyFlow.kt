package com.formssihk.sample.flow

import co.paralleluniverse.fibers.Suspendable
import com.formssihk.sample.contract.IOUContract
import com.formssihk.sample.state.IOUState
import com.formssihk.sample.utils.myIdentity
import net.corda.core.contracts.StateRef
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.loggerFor
import org.slf4j.Logger

@StartableByRPC
class ModifyFlow(val ref: StateRef, val value: Int, val remarks: String) : IOUFlow(){
    override val log: Logger = loggerFor<CreateFlow>()
    override  val command = IOUContract.Commands.Update()

    @Suspendable
    override fun call(): SignedTransaction {
        val iouState = serviceHub.toStateAndRef<IOUState>(ref)
        val newIouState = iouState.state.data.copy(creator = serviceHub.myIdentity(), value = value, remark = remarks)

        return defaultFlow(newIouState, iouState)
    }
}