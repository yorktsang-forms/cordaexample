package com.atfx.sample.flow;

import co.paralleluniverse.fibers.Suspendable;
import com.atfx.sample.utils.CordaUtils;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.contracts.StateRef;
import net.corda.core.flows.*;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.ProgressTracker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@InitiatingFlow
public class FinalizeFlow extends FlowLogic<SignedTransaction> {

    private SignedTransaction stx;
    ProgressTracker progressTracker;

    public FinalizeFlow(SignedTransaction stx) {
        this(stx, FinalityFlow.tracker());
    }

    public FinalizeFlow(SignedTransaction stx,
                        ProgressTracker progressTracker) {
        super();
        this.stx = stx;
        this.progressTracker = progressTracker;
    }

    @Override
    @Suspendable
    public SignedTransaction call() throws FlowException {
        List<StateAndRef<ContractState>> inputStates = new ArrayList<>();
        for (StateRef inputState : stx.getInputs()) {
            inputStates.add(getServiceHub().toStateAndRef(inputState));
        }
        List<AbstractParty> inParticipants = inputStates.stream()
                .flatMap(it -> it.getState().getData().getParticipants().stream())
                .collect(Collectors.toList());

        List<AbstractParty> outParticipants =
                stx.getTx().outRefsOfType(ContractState.class).stream()
                        .flatMap(it -> it.getState().getData().getParticipants().stream())
                        .collect(Collectors.toList());

        ArrayList<AbstractParty> allParticipants = new ArrayList<>(inParticipants);
        allParticipants.addAll(outParticipants);

        HashSet<Party> allParty = new HashSet<>();
        for(AbstractParty ap: allParticipants) {
            Party party = getServiceHub().getIdentityService()
                    .requireWellKnownPartyFromAnonymous(ap);
            allParty.add(party);
        }
        allParty.remove(CordaUtils.getServiceHubMyIdentity(getServiceHub()));

        ArrayList<FlowSession> sessions = new ArrayList<>();
        for (Party party: allParty) {
            sessions.add(initiateFlow(party));
        }
        return subFlow(new FinalityFlow(stx, sessions));
    }

    public ProgressTracker tracker() {
        return FinalityFlow.tracker();
    }
}
