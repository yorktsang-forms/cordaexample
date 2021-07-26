package com.formssihk.sample.flow

import com.formssihk.sample.state.IOUState
import com.formssihk.sample.utils.TestParties
import com.formssihk.sample.utils.TestUtil
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.utilities.loggerFor
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModifyFlowTest {
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
    fun ModifyFlowBySelf() {
        TestUtil.runFlow(tn, tn.partyA.startFlow(CreateFlow(10, "asd")))
        val result = tn.partyA.transaction { tn.partyA.services.vaultService.queryBy(IOUState::class.java, QueryCriteria.VaultQueryCriteria()).states }
        assertEquals( 1, result.size)

        val stateRef = result.first().ref
        TestUtil.runFlow(tn, tn.partyA.startFlow(ModifyFlow( stateRef , 12, "asd")))
        val resultB = tn.partyB.transaction { tn.partyB.services.vaultService.queryBy(IOUState::class.java, QueryCriteria.VaultQueryCriteria()).states }
        assertEquals( 1, resultB.size)
        assertEquals(12, resultB.first().state.data.value)
    }

    @Test
    fun ModifyFlowByOther() {
        TestUtil.runFlow(tn, tn.partyA.startFlow(CreateFlow(10, "asd")))
        val result = tn.partyA.transaction { tn.partyA.services.vaultService.queryBy(IOUState::class.java, QueryCriteria.VaultQueryCriteria()).states }
        assertEquals( 1, result.size)

        val stateRef = result.first().ref
        TestUtil.runFlow(tn, tn.partyB.startFlow(ModifyFlow( stateRef , 12, "asd")))
        val result2 = tn.partyA.transaction { tn.partyB.services.vaultService.queryBy(IOUState::class.java, QueryCriteria.VaultQueryCriteria()).states }
        assertEquals( 1, result2.size)
        assertEquals(12, result2.first().state.data.value)
    }
}