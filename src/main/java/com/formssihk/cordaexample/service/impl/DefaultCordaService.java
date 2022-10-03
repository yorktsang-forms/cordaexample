package com.formssihk.cordaexample.service.impl;

import com.formssihk.cordaexample.service.CordaService;
import com.formssihk.cordaexample.utils.JarConverter;
import com.formssihk.sample.flow.CreateFlow;
import com.formssihk.sample.flow.ModifyFlow;
import lombok.var;
import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.concurrent.CordaFuture;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.contracts.StateRef;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.crypto.SecureHash;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.messaging.ClientRpcSslOptions;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.node.NodeInfo;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.PageSpecification;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.NetworkHostAndPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.corda.core.node.services.vault.*;

public class DefaultCordaService implements CordaService {


    private final int DEFAULT_PAGE_NUM = 1;

    private Logger log = LoggerFactory.getLogger(DefaultCordaService.class);

    @Value("${config.rpc.host:localhost}")
    String nodeHost = "localhost";

    @Value("${config.rpc.port:10006}")
    String nodePort = "10006";

    @Value("${config.rpc.username}")
    String nodeLoginName = "user1";

    @Value("${config.rpc.password}")
    String nodeLoginPsw = "test";

    @Value("${config.rpc.usetls:false}")
    boolean usetls = false;

    @Value("${config.rpc.certPath:rpccerts}")
    Path certsPath = Paths.get("reccerts");

    @Value("${config.rpc.certsPass:abcabc}")
    String certPass = "abcabc";

    private CordaRPCOps cordaRPCOps;

    private CordaRPCConnection connection;

    int vaultPageSize = 2000;

    @PostConstruct
    @Override
    public void getOrCreateConnection() {

        if (connection == null) {
            createCordaRPCOps();
        }

        try {
            cordaRPCOps.currentNodeTime();
        } catch (Exception e) {
            createCordaRPCOps();
        }
    }

    private void createCordaRPCOps() {

        NetworkHostAndPort nodeHP = new NetworkHostAndPort(nodeHost, Integer.parseInt(nodePort));
        log.debug("Upstream node URL: {}", nodeHP.getHost());

        CordaRPCClient client = null;
        if (usetls) {
            client = new CordaRPCClient(
                    nodeHP,
                    new ClientRpcSslOptions(
                            certsPath,
                            certPass,
                            "JKS"
                    ),
                    null);
        } else {
            client = new CordaRPCClient(nodeHP);
        }

        log.debug("Attempting upstream RPC to node: '{}'", nodeHP);
        connection = client.start(nodeLoginName, nodeLoginPsw);
        log.debug("Successful connection. Creating proxy...");

        cordaRPCOps = connection.getProxy();
    }

    @Override
    public void closeConnection() {
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public NodeInfo nodeInfo() {
        getOrCreateConnection();
        return cordaRPCOps.nodeInfo();
    }

    @Override
    public Party partyByName(CordaX500Name name) {
        getOrCreateConnection();
        if (cordaRPCOps.wellKnownPartyFromX500Name(name) != null) {
            return cordaRPCOps.wellKnownPartyFromX500Name(name);
        } else {
            throw new IllegalArgumentException(String.format("Party %s not found in network map", name));
        }
    }

    @Override
    public List<Party> parties() {
        getOrCreateConnection();
        return cordaRPCOps.networkMapSnapshot().stream().map(it -> it.getLegalIdentities().get(0)).collect(Collectors.toList());
    }

    @Override
    public CordaRPCOps rpc() {
        getOrCreateConnection();
        return cordaRPCOps;
    }

    @Override
    public <T extends ContractState> StateAndRef<T> getState(StateRef ref, Class<T> clazz) {
        // TODO: query criteria
//        getOrCreateConnection();
//
//        CordaRPCOps cordaRPCOps = this.cordaRPCOps;
//
//
//        var queryCriteria = (QueryCriteria.VaultQueryCriteria) new QueryCriteria.VaultQueryCriteria(
//                Vault.StateStatus.ALL,
//                null,
//                new ArrayList<StateRef>(Arrays.asList(ref)),
//                null,
//                null,
//                null
//        );
//
//        cordaRPCOps.vaultQueryByCriteria(
//                (QueryCriteria) queryCriteria,
//                clazz
//        ).getStates();
        return null;
    }

    @Override
    public <T extends ContractState> StateAndRef<T> getStateByLinearId(UniqueIdentifier linearId, Class<T> clazz) {
        // TODO: query criteria

        return null;
    }

    @Override
    public <T extends ContractState> List<StateAndRef<T>> getAllState(Class<T> clazz) {
        // TODO: query criteria

//        getOrCreateConnection();
//        Vault.Page<T> page = cordaRPCOps.vaultQueryByWithPagingSpec(
//                clazz,
//                new QueryCriteria.VaultQueryCriteria(
//                        Vault.StateStatus.UNCONSUMED
//                ),
//                new PageSpecification(DEFAULT_PAGE_NUM, vaultPageSize)
//        );
//        return page.getStates();
        return null;
    }

    @Override
    public <T extends ContractState> List<StateAndRef<T>> getAllStatesByLinearId(UniqueIdentifier linearId, Class<T> clazz) {
        // TODO: query criteria

        return null;
    }

    @Override
    public <T extends ContractState> StateAndRef<T> getLatestStateByLinearId(UniqueIdentifier linearId, Class<T> clazz) {
        // TODO: query criteria

        return null;
    }

    @Override
    public SecureHash uploadAttachment(InputStream uploadedInputStream) {

        getOrCreateConnection();


        InputStream is = JarConverter.createJar(uploadedInputStream);

        try {
            return cordaRPCOps.uploadAttachment(is);
        } catch (FileAlreadyExistsException e) {
            return null;
        }
    }

    @Override
    public void downloadAttachment(SecureHash hash, OutputStream dest) {

        getOrCreateConnection();
        JarConverter.extractDocument(cordaRPCOps.openAttachment(hash), dest);

    }

    private SignedTransaction runWorkflow(Supplier<CordaFuture<SignedTransaction>> op) throws ExecutionException, InterruptedException {
        getOrCreateConnection();
        return op.get().get();
    }

    @Override
    public SignedTransaction createIOU(String userPublicKey, Instant creationTime, Map<String, Instant> loadRecords) {
        try {
            return runWorkflow(() -> cordaRPCOps.startFlowDynamic(
                            CreateFlow.class,
                            userPublicKey,
                            creationTime,
                            new HashMap<String, Instant>())
                    .getReturnValue()
            );
        } catch (Exception e) {
            log.error("Error occur during create IOU", e);
        }
        return null;
    }

    @Override
    public SignedTransaction modifyIOU(StateRef ref, Map<String, Instant> loadRecords) {

        try {
            return runWorkflow(() -> cordaRPCOps.startFlowDynamic(
                            ModifyFlow.class,
                            ref,
                            loadRecords)
                    .getReturnValue()
            );
        } catch (Exception e) {
            log.error("Error occur during modify IOU", e);
        }

        return null;
    }

}
