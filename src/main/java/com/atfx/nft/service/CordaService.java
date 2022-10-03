package com.atfx.nft.service;

import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.contracts.StateRef;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.crypto.SecureHash;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.node.NodeInfo;
import net.corda.core.transactions.SignedTransaction;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface CordaService {

    void getOrCreateConnection();
    void closeConnection();
    NodeInfo nodeInfo();

    Party partyByName(CordaX500Name name);

    List<Party> parties();
    CordaRPCOps rpc();

    <T extends ContractState> StateAndRef<T> getState(StateRef ref, Class<? extends T> clazz);
    <T extends ContractState> StateAndRef<T> getStateByLinearId(UniqueIdentifier linearId, Class<T> clazz);

    <T extends ContractState> List<StateAndRef<T>> getAllState(Class<T> clazz);
    <T extends ContractState> List<StateAndRef<T>> getAllStatesByLinearId(UniqueIdentifier linearId, Class<T> clazz);
    <T extends ContractState> StateAndRef<T> getLatestStateByLinearId(UniqueIdentifier linearId, Class<T> clazz);

    SecureHash uploadAttachment(InputStream uploadedInputStream);
    void downloadAttachment(SecureHash hash, OutputStream dest);

    SignedTransaction createIOU(String userPublicKey, Instant creationTime, Map<String, Instant> loadRecords);

    SignedTransaction modifyIOU(StateRef ref, Map<String, Instant> loadRecords);
}
