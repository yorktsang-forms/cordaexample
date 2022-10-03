package com.atfx.sample.contract;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

public class IOUContract implements Contract {
    private final static String ID = "io.cryptoblk.sample.contract.IOUContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {

        if (!(tx.getOutputs().size() == 1)) {
            throw new IllegalArgumentException("Failed requirement: ???");
        }

        //if (!(tx.outputsOfType(IOUState.class.get)== 1)) {
        //    throw new IllegalArgumentException("Failed requirement: ???");
        //}
    }

    public interface Commands extends CommandData {
        class Create implements Commands {}
        class Update implements Commands {}
    }

}
