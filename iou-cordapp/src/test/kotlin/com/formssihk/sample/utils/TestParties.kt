package com.formssihk.sample.utils

import net.corda.core.identity.Party
import net.corda.testing.core.ALICE_NAME
import net.corda.testing.core.BOB_NAME
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.StartedMockNode
import net.corda.testing.node.internal.findCordapp


class TestParties {
    var isSetup = false
    lateinit var net: MockNetwork
    lateinit var partyA: StartedMockNode
    lateinit var partyB: StartedMockNode
    lateinit var notary: Party
    lateinit var nodes: List<StartedMockNode>

    fun setup() {
        net = MockNetwork(MockNetworkParameters(listOf(findCordapp("com.formssihk.sample"))))
        partyA = net.createPartyNode(ALICE_NAME)
        partyB = net.createPartyNode(BOB_NAME)
        notary = net.defaultNotaryIdentity
        nodes = listOf(partyA, partyB)
        net.runNetwork()
        isSetup = true
    }

    fun tearDown() {
        if(isSetup)  net.stopNodes()
    }
}