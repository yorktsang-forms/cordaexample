package com.formssihk.sample.schema;

import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class IOUSchemaV1 extends MappedSchema {
    public IOUSchemaV1(@NotNull Class<?> schemaFamily,
                       int version,
                       @NotNull Iterable<? extends Class<?>> mappedTypes) {
        super(schemaFamily, version, mappedTypes);
    }

    public IOUSchemaV1() {
        this(IOUSchema.class.getClass(), 1,
                new ArrayList<>(Arrays.asList(PersistentIOU.class.getClass())));
    }
}
