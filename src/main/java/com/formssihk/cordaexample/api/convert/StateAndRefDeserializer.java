package com.formssihk.cordaexample.api.convert;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import net.corda.core.contracts.StateAndRef;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class StateAndRefDeserializer extends JsonDeserializer {
    @Override
    public StateAndRef deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.readValueAs(StateAndRef.class);
    }
}
