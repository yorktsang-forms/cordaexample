package com.formssihk.cordaexample.service.impl

import com.formssihk.cordaexample.service.CordaService
import com.formssihk.cordaexample.service.CordaService1
import com.formssihk.cordaexample.utils.JarConverter
import com.formssihk.sample.flow.CreateFlow
import com.formssihk.sample.flow.ModifyFlow
import net.corda.client.rpc.CordaRPCClient
import net.corda.client.rpc.CordaRPCConnection
import net.corda.core.concurrent.CordaFuture
import net.corda.core.contracts.ContractState
import net.corda.core.contracts.StateAndRef
import net.corda.core.contracts.StateRef
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.crypto.SecureHash
import net.corda.core.identity.CordaX500Name
import net.corda.core.identity.Party
import net.corda.core.messaging.ClientRpcSslOptions
import net.corda.core.messaging.CordaRPCOps
import net.corda.core.messaging.startFlow
import net.corda.core.node.NodeInfo
import net.corda.core.node.services.Vault
import net.corda.core.node.services.vault.DEFAULT_PAGE_NUM
import net.corda.core.node.services.vault.PageSpecification
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.NetworkHostAndPort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Instant
import javax.annotation.PostConstruct
import kotlin.reflect.KClass

@Service
class DefaultCordaService : CordaService1 {
    private val log = LoggerFactory.getLogger(DefaultCordaService::class.java)

    @Value("\${config.rpc.host:localhost}")
    val nodeHost = "localhost"

    @Value("\${config.rpc.port:10006}")
    val nodePort = "10006"

    @Value("\${config.rpc.username}")
    val nodeLoginName = "user1"

    @Value("\${config.rpc.password}")
    val nodeLoginPsw = "test"

    @Value("\${config.rpc.usetls:false}")
    val usetls = false

    @Value("\${config.rpc.certsPath:rpccerts}")
    val certsPath: Path = Paths.get("rpccerts")

    @Value("\${config.rpc.certsPass:abcabc}")
    val certsPass = "abcabc"

    private lateinit var cordaRPCOps: CordaRPCOps
    private var connection: CordaRPCConnection? = null
    val vaultPageSize = 2000

    @PostConstruct
    override fun getOrCreateConnection() {
        if (connection == null) {
            createCordaRPCOps()
        }
        // Test the connection
        try {
            cordaRPCOps.currentNodeTime()
        } catch (e: Exception) {
            createCordaRPCOps()
        }
    }

    /**
     * Provide the upstream Corda node client RPC proxy.
     */
    private fun createCordaRPCOps() {
        // Verify the node URL and extract as a HostAndPort
        val nodeHP = NetworkHostAndPort(nodeHost, nodePort.toInt())
        log.debug("Upstream node URL:'${nodeHP.host}'")

        val client = if (usetls)
            CordaRPCClient(nodeHP, ClientRpcSslOptions(certsPath, certsPass, "JKS"))
        else CordaRPCClient(nodeHP)

        // Start the RPC client using the first of the RPC users
        log.debug("Attempting upstream RPC to node:'${nodeHP}'")
        connection = client.start(nodeLoginName, nodeLoginPsw)
        log.debug("Successful connection. Creating proxy...")

        // Keep a reference to this client for this node
        cordaRPCOps = connection!!.proxy
    }

    override fun closeConnection() {
        connection?.close()
    }

    override fun nodeInfo(): NodeInfo {
        getOrCreateConnection()
        return cordaRPCOps.nodeInfo()
    }

    override fun partyByName(name: CordaX500Name): Party {
        getOrCreateConnection()
        return cordaRPCOps.wellKnownPartyFromX500Name(name)
            ?: throw IllegalArgumentException("Party $name not found in network map.")
    }

    override fun parties(): List<Party> {
        getOrCreateConnection()
        return cordaRPCOps.networkMapSnapshot().map {
            it.legalIdentities.first()
        }
    }

    override fun rpc(): CordaRPCOps {
        getOrCreateConnection()
        return cordaRPCOps
    }

    override fun <T : ContractState> getState(ref: StateRef, clazz: KClass<T>): StateAndRef<T> {
        getOrCreateConnection()
        return cordaRPCOps.vaultQueryByCriteria(QueryCriteria.VaultQueryCriteria(stateRefs = listOf(ref), status = Vault.StateStatus.ALL), clazz.java)
            .states.singleOrNull() ?: throw NoSuchElementException("ref=$ref")
    }

    override fun <T : ContractState> getStateByLinearId(linearId: UniqueIdentifier, clazz: KClass<T>): StateAndRef<T> {
        getOrCreateConnection()
        val page = cordaRPCOps.vaultQueryByCriteria(QueryCriteria.LinearStateQueryCriteria(linearId = listOf(linearId), status = Vault.StateStatus.UNCONSUMED), clazz.java)
        return page.states.singleOrNull() ?: throw NoSuchElementException("linearId=$linearId")
    }

    override fun <T : ContractState> getAllStates(clazz: KClass<T>): List<StateAndRef<T>> {
        getOrCreateConnection()
        val page = cordaRPCOps.vaultQueryByWithPagingSpec(clazz.java, QueryCriteria.VaultQueryCriteria(Vault.StateStatus.UNCONSUMED)
            , PageSpecification(DEFAULT_PAGE_NUM, vaultPageSize)
        )
        return page.states
    }

    override fun <T : ContractState> getAllStatesByLinearId(
        linearId: UniqueIdentifier,
        clazz: KClass<T>
    ): List<StateAndRef<T>> {
        getOrCreateConnection()
        val page = cordaRPCOps.vaultQueryByCriteria(QueryCriteria.LinearStateQueryCriteria(linearId = listOf(linearId), status = Vault.StateStatus.ALL), clazz.java)
        return page.states
    }

    override fun <T : ContractState> getLatestStateByLinearId(
        linearId: UniqueIdentifier,
        clazz: KClass<T>
    ): StateAndRef<T> {
        getOrCreateConnection()
        val page = cordaRPCOps.vaultQueryByCriteria(QueryCriteria.LinearStateQueryCriteria(linearId = listOf(linearId), status = Vault.StateStatus.UNCONSUMED), clazz.java)
        return page.states.singleOrNull() ?: throw NoSuchElementException("linearId=$linearId")
    }

    override fun uploadAttachment(uploadedInputStream: InputStream): SecureHash {
        getOrCreateConnection()
        return JarConverter.createJar(uploadedInputStream).use {
            cordaRPCOps.uploadAttachment(it)
        }
    }

    override fun downloadAttachment(hash: SecureHash, dest: OutputStream) {
        getOrCreateConnection()
        JarConverter.extractDocument(cordaRPCOps.openAttachment(hash), dest)
    }

    private fun runWorkflow(op: () -> CordaFuture<SignedTransaction>): SignedTransaction {
        getOrCreateConnection()
        val result = op().get()

        return result
    }

    override fun createIOU(userPublicKey: String, creationTime: Instant, loanRecords: Map<String, Instant>) : SignedTransaction {
        return runWorkflow { cordaRPCOps.startFlow(::CreateFlow, userPublicKey, creationTime, emptyMap()).returnValue }
    }

    override fun modifyIOU(ref: StateRef, loanRecords: Map<String, Instant>) : SignedTransaction {
        return runWorkflow { cordaRPCOps.startFlow(::ModifyFlow, ref, loanRecords).returnValue }
    }
}