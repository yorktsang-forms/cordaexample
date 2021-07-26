package com.formssihk.sample.flow

import co.paralleluniverse.fibers.Suspendable
import com.formssihk.sample.contract.IOUContract
import com.formssihk.sample.state.IOUState
import com.formssihk.sample.utils.myIdentity
import com.formssihk.sample.utils.preferredNotary
import net.corda.core.contracts.StateAndRef
import net.corda.core.flows.FlowLogic
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import net.corda.core.utilities.seconds
import org.slf4j.Logger

abstract class IOUFlow : FlowLogic<SignedTransaction>() {
    companion object {
        object GENERATING_APPLICATION_TRANSACTION : ProgressTracker.Step("Generating application transaction.")
        object SIGNING_TRANSACTION : ProgressTracker.Step("Signing transaction with our key.")
        object FINALIZING : ProgressTracker.Step("Recording and distributing transaction.")

        fun tracker() = ProgressTracker(
            GENERATING_APPLICATION_TRANSACTION,
            SIGNING_TRANSACTION,
            FINALIZING
        )
    }

    override val progressTracker = tracker()
    abstract val log: Logger
    abstract val command: IOUContract.Commands

    fun setStep(step: ProgressTracker.Step) {
        progressTracker.currentStep = step
        log.info("Submit: ${progressTracker.currentStep.label}")
    }

    @Suspendable
    fun defaultFlow(outputState: IOUState, prev: StateAndRef<IOUState>?): SignedTransaction {
        val notary = prev?.state?.notary ?: serviceHub.preferredNotary()
        val myKey = serviceHub.myIdentity().owningKey

        // #1 Generate transaction
        progressTracker.currentStep = GENERATING_APPLICATION_TRANSACTION

        //generate transaction
        val tx = TransactionBuilder(notary).apply {
            prev?.run { addInputState(prev) }
            addOutputState(outputState, IOUContract::class.qualifiedName!!)
            addCommand(command, myKey)
        }

        // #2 Sign transaction
        progressTracker.currentStep = SIGNING_TRANSACTION
        val currentTime = serviceHub.clock.instant()
        tx.setTimeWindow(currentTime, 30.seconds)
        val stx = serviceHub.signInitialTransaction(tx)

        // #3 Finalize
        progressTracker.currentStep = FINALIZING
        return subFlow(FinalizeFlow(stx))
    }

}