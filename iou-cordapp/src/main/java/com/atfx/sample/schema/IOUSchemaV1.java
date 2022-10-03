package com.atfx.sample.schema;

import net.corda.core.schemas.MappedSchema;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

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
