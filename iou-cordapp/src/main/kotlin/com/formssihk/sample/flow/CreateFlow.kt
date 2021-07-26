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

@StartableByRPC
class CreateFlow(val value: Int, val remarks: String) : IOUFlow(){
    override val log: Logger = loggerFor<CreateFlow>()
    override  val command = IOUContract.Commands.Create()

    @Suspendable
    override fun call(): SignedTransaction {
        val newState = IOUState(value,
            serviceHub.myIdentity(),
            serviceHub.allParties(),
            remarks
        )

        return defaultFlow(newState,null)
    }
}