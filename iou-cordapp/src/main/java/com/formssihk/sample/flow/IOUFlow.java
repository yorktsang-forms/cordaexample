package com.formssihk.sample.flow;

import com.formssihk.sample.contract.IOUContract;
import com.formssihk.sample.state.IOUState;
import com.formssihk.sample.utils.CordaUtils;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import org.slf4j.Logger;

import java.security.PublicKey;
import java.time.Duration;
import java.time.Instant;

public abstract class IOUFlow extends FlowLogic<SignedTransaction> {
    public static ProgressTracker.Step GENERATING_APPLICATION_TRANSACTION
            = new ProgressTracker.Step("Generating application transaction.");

    public static ProgressTracker.Step SIGNING_TRANSACTION
            = new ProgressTracker.Step("Signing transaction with our key.");

    public static ProgressTracker.Step FINALIZING
            = new ProgressTracker.Step("Recording and distributing transaction.");

    public static ProgressTracker tracker() {
        return new ProgressTracker(
                GENERATING_APPLICATION_TRANSACTION,
                SIGNING_TRANSACTION,
                FINALIZING
        );
    }

    public ProgressTracker progressTracker = tracker();
    protected Logger logger;
    protected IOUContract.Commands command;

    public SignedTransaction defaultFlow (IOUState outputState,
                                          StateAndRef<IOUState> prev) throws FlowException {
        Party notary = null;
        if(prev != null) {
            if(prev.getState() != null) {
                if(prev.getState().getNotary() != null) {
                    notary = prev.getState().getNotary();
                } else {
                    notary = CordaUtils.getServiceHubPreferredNotary(getServiceHub());
                }
            }
        }

        progressTracker.setCurrentStep(GENERATING_APPLICATION_TRANSACTION);
        PublicKey myKey = CordaUtils.getServiceHubMyIdentity(getServiceHub()).getOwningKey();

        TransactionBuilder tx = new TransactionBuilder(notary);
        if(prev != null) {
            tx.addInputState(prev);
        }
        tx.addOutputState(outputState);
        tx.addCommand(command, myKey);

        progressTracker.setCurrentStep(SIGNING_TRANSACTION);
        Instant currentTime = getServiceHub().getClock().instant();
        tx.setTimeWindow(currentTime, Duration.ofSeconds(30));
        SignedTransaction stx = getServiceHub().signInitialTransaction(tx);

        progressTracker.setCurrentStep(FINALIZING);
        return subFlow(new FinalizeFlow(stx));
    }
}
