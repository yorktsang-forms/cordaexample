package com.formssihk.cordaexample.api.convert;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import net.corda.core.contracts.StateRef;
import net.corda.core.crypto.SecureHash;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@JsonComponent
public class StateRefDeserializer extends JsonDeserializer<StateRef> implements Converter<String, StateRef> {

    public static StateRef parseString(String str) {
        List<String> parts = Arrays.asList(str.split("[\\(||\\)]"));
        return new StateRef(SecureHash.parse(parts.get(0)), Integer.parseInt(parts.get(1)));
    }

    @Override
    public StateRef deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String str = p.readValueAs(String.class);
        return parseString(str);
    }

    @Override
    public StateRef convert(String str) {
        return parseString(str);
    }

}
