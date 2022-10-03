package com.atfx.sample.schema;

import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.Builder;
import net.corda.core.node.services.vault.QueryCriteria;

public class IOUContractCriteria {
    public QueryCriteria getUserPublicKeyEqualCriteria(String value,
                                                       Vault.StateStatus status) {
        try {
            return new QueryCriteria.VaultCustomQueryCriteria(Builder.equal(
                    PersistentIOU.class.getField("myPublicKey"), value), status);
        } catch (NoSuchFieldException e) {
            //TODO
           //return new QueryCriteria.LinearStateQueryCriteria(null, null, null, Vault.StateStatus.UNCONSUMED, null, Vault.RelevancyStatus.ALL, null);
           return null;
        }
    }
    public QueryCriteria getUserPublicKeyEqualCriteria(String value) {
        return getUserPublicKeyEqualCriteria(value, Vault.StateStatus.UNCONSUMED);
    }
}
