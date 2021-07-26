package com.formssihk.sample.flow

import co.paralleluniverse.fibers.Suspendable
import net.corda.core.contracts.ContractState
import net.corda.core.flows.*
import net.corda.core.node.ServiceHub
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker

@InitiatingFlow
class FinalizeFlow(
    private val stx: SignedTransaction,
    progressTracker: ProgressTracker = FinalityFlow.tracker()
): FlowLogic<SignedTransaction>() {
    companion object {
        fun tracker() = FinalityFlow.tracker()
    }

    @Suspendable
    override fun call(): SignedTransaction {
        val inParticipants = stx.inputs.map { serviceHub.toStateAndRef<ContractState>(it) }.flatMap { it.state.data.participants }
        val outParticipants = stx.tx.outRefsOfType<ContractState>().flatMap { it.state.data.participants }
        val allParticipants = inParticipants + outParticipants
        val sessions = allParticipants.map { serviceHub.identityService.requireWellKnownPartyFromAnonymous(it) }.distinct()
            .filterNot { it == serviceHub.myInfo.legalIdentities.first() }
            .map { initiateFlow(it) }
        return subFlow(FinalityFlow(stx, sessions))
    }

}

@InitiatedBy(FinalizeFlow::class)
class ReceiveFinalizeFlow(val otherSide: FlowSession) : FlowLogic<SignedTransaction>() {
    @Suspendable
    override fun call() = subFlow(ReceiveFinalityFlow(otherSide))
}

fun TransactionBuilder.fillContractAttachments(serviceHub: ServiceHub) {
    /*
    listOf(
        // serviceHub.attachments.internalFindTrustedAttachmentForClass("com/r3/demos/ubin2a/base/WalletType")?.id,
        //serviceHub.attachments.getLatestContractAttachments(Cash.PROGRAM_ID).firstOrNull(),
        //serviceHub.attachments.getLatestContractAttachments(Instruction.PROGRAM_ID).firstOrNull(),
        //serviceHub.attachments.getLatestContractAttachments("th.or.bot.inthanon.bond.bond.contracts.Bond").firstOrNull(),
        //serviceHub.attachments.getLatestContractAttachments("th.or.bot.inthanon.mlsm.contract.ProposeRequest").firstOrNull(),
        //serviceHub.attachments.getLatestContractAttachments("th.or.bot.inthanon.scheduler.contracts.SchedulerJob").firstOrNull(),
        //serviceHub.attachments.getLatestContractAttachments("th.or.bot.inthanon.remittance.contracts.Remittance").firstOrNull()
    ).filter { it != null }.forEach {
        this.addAttachment(it!!)
    }*/
}