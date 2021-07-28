package com.formssihk.cordaexample.service

import net.corda.core.concurrent.CordaFuture
import net.corda.core.contracts.ContractState
import net.corda.core.contracts.StateAndRef
import net.corda.core.contracts.StateRef
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.crypto.SecureHash
import net.corda.core.identity.CordaX500Name
import net.corda.core.identity.Party
import net.corda.core.messaging.CordaRPCOps
import net.corda.core.node.NodeInfo
import net.corda.core.transactions.SignedTransaction
import java.io.InputStream
import java.io.OutputStream
import java.time.Instant
import kotlin.reflect.KClass

interface CordaService {
    fun getOrCreateConnection()
    fun closeConnection()
    fun nodeInfo(): NodeInfo
    fun partyByName(name: CordaX500Name): Party
    fun parties(): List<Party>
    fun rpc(): CordaRPCOps
    fun <T : ContractState> getState(ref: StateRef, clazz: KClass<T>): StateAndRef<T>
    fun <T : ContractState> getStateByLinearId(linearId: UniqueIdentifier, clazz: KClass<T>): StateAndRef<T>
    fun <T : ContractState> getAllStates(clazz: KClass<T>): List<StateAndRef<T>>
    fun <T : ContractState> getAllStatesByLinearId(linearId: UniqueIdentifier, clazz: KClass<T>): List<StateAndRef<T>>
    fun <T : ContractState> getLatestStateByLinearId(linearId: UniqueIdentifier, clazz: KClass<T>): StateAndRef<T>
    fun uploadAttachment(uploadedInputStream: InputStream): SecureHash
    fun downloadAttachment(hash: SecureHash, dest: OutputStream)
    fun createIOU(userPublicKey: String, creationTime: Instant, loanRecords: Map<String, Instant>): SignedTransaction
    fun modifyIOU(ref: StateRef, loanRecords: Map<String, Instant>): SignedTransaction
}