package com.formssihk.sample.flow;

import co.paralleluniverse.fibers.Suspendable;
import com.formssihk.sample.contract.IOUContract;
import com.formssihk.sample.state.IOUState;
import com.formssihk.sample.utils.CordaUtils;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.contracts.StateRef;
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
public class ModifyFlow extends IOUFlow{
    Logger log = LoggerFactory.getLogger(CreateFlow.class);

    StateRef ref;
    Map<String, Instant> loanRecords = new HashMap<>();

    public ModifyFlow(StateRef ref, Map<String, Instant> loanRecords) {
        super();
        this.ref = ref;
        this.loanRecords = loanRecords;
        command = new IOUContract.Commands.Update();
    }

    @Override
    @Suspendable
    public SignedTransaction call() throws FlowException {
        StateAndRef<IOUState> iouState = getServiceHub().<IOUState>toStateAndRef(ref);
        Map<String, Instant> prevLoanRecords = iouState.getState().getData().getLoanRecords();

        HashMap<String, Instant> allLoanRecords = new HashMap<>();
        allLoanRecords.putAll(prevLoanRecords);
        allLoanRecords.putAll(loanRecords);

        IOUState newIouState = iouState.getState().getData().deepCopy();
        newIouState.setLoanRecords(allLoanRecords);
        newIouState.setLastModifiedTime(Instant.now());

        return defaultFlow(newIouState, iouState);
    }
}
