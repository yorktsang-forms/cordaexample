package com.atfx.sample.flow;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

@InitiatedBy(FinalizeFlow.class)
public class ReceiveFinalizeFlow extends FlowLogic<SignedTransaction> {
    FlowSession otherSide;

    public ReceiveFinalizeFlow(FlowSession otherSide) {
        super();
    }

    @Override
    @Suspendable
    public SignedTransaction call() throws FlowException {
        return subFlow(new ReceiveFinalityFlow(otherSide));
    }
}
