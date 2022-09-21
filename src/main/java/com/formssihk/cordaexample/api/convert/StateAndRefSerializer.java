package com.formssihk.cordaexample.api.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndRef;

import java.util.HashMap;

public class StateAndRefSerializer extends JsonSerializer<StateAndRef> {
    @Override
    public void serialize(StateAndRef value, JsonGenerator gen, SerializerProvider serializers) {
        ContractState data = value.getState().getData();
        HashMap<String,Object> json = new HashMap<String, Object>();
                json.put("ref", value.getRef());
                json.put("_data", data);
                json.put("_type", data.getClass().getSimpleName());

    }

}
