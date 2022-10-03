package com.atfx.sample.flow;

import co.paralleluniverse.fibers.Suspendable;
import com.atfx.sample.contract.IOUContract;
import com.atfx.sample.state.IOUState;
import com.atfx.sample.utils.CordaUtils;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.transactions.SignedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@StartableByRPC
public class CreateFlow extends IOUFlow {
    Logger log = LoggerFactory.getLogger(CreateFlow.class);

    String userPublicKey;
    Instant creationTime;
    Map<String, Instant> loanRecords = new HashMap<>();

    public CreateFlow(String userPublicKey, Instant creationTime,
                      Map<String, Instant> loanRecords) {
        super();
        this.userPublicKey = userPublicKey;
        this.creationTime = creationTime;
        this.loanRecords = loanRecords;
        command = new IOUContract.Commands.Create();
    }

    @Override
    @Suspendable
    public SignedTransaction call() throws FlowException {
        IOUState newState = new IOUState(
                userPublicKey,
                loanRecords,
                creationTime,
                Instant.now(),
                CordaUtils.getServiceHubAllParties(getServiceHub()),
                new UniqueIdentifier()
        );
        return defaultFlow(newState, null);
    }
}
