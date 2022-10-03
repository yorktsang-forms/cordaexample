package com.formssihk.sample.flow

import com.formssihk.sample.state.IOUState
import com.formssihk.sample.utils.TestParties
import com.formssihk.sample.utils.TestUtil.runFlow
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.utilities.loggerFor
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals

class CreateFlowTest {
    val tn = TestParties()

    private val log = loggerFor<CreateFlowTest>()

    @BeforeEach
    fun setup() {
        tn.setup()
    }

    @AfterEach
    fun tearDown() {
        tn.tearDown()
    }

    @Test
    fun createFlow() {
        runFlow(tn, tn.partyA.startFlow(CreateFlow("asdasd", Instant.now(), mapOf("row1" to Instant.now()))))
        val result = tn.partyA.transaction { tn.partyA.services.vaultService.queryBy(IOUState::class.java, QueryCriteria.VaultQueryCriteria()).states }
        assertEquals( 1, result.size)

        val resultB = tn.partyB.transaction { tn.partyB.services.vaultService.queryBy(IOUState::class.java, QueryCriteria.VaultQueryCriteria()).states }
        assertEquals( 1, resultB.size)
    }
}