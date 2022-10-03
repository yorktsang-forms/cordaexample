package com.atfx.sample.state;

import com.atfx.sample.contract.IOUContract;
import com.atfx.sample.schema.IOUSchemaV1;
import com.atfx.sample.schema.PersistentIOU;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The state object recording IOU agreements between two parties.
 *
 * A state must implement [ContractState] or one of its descendants.
 *
 */
@BelongsToContract(IOUContract.class)
@AllArgsConstructor
@Data
public class IOUState implements LinearState, QueryableState {
    String userPublicKey = "";
    Map<String, Instant> loanRecords = new HashMap<>();
    Instant creationTime = Instant.now();
    Instant lastModifiedTime = Instant.now();
    List<Party > parties = new ArrayList<>();
    UniqueIdentifier linearId = new UniqueIdentifier();

    @Override
    public List<AbstractParty> getParticipants() {
        return new ArrayList<>(parties);
    }

    @NotNull
    @Override
    public PersistentState generateMappedObject(@NotNull MappedSchema schema) {
        if(schema instanceof IOUSchemaV1) {
            return new PersistentIOU(userPublicKey, linearId.getId());
        } else {
            throw new IllegalArgumentException("Unrecognised schema $schema");
        }
    }

    @NotNull
    @Override
    public Iterable<MappedSchema> supportedSchemas() {
        ArrayList<MappedSchema> result = new ArrayList<>();
        result.add(new IOUSchemaV1());
        return result;
    }

    public IOUState deepCopy() {
        return new IOUState(
                userPublicKey,
                loanRecords,
                creationTime,
                lastModifiedTime,
                parties,
                linearId
        );
    }
}
