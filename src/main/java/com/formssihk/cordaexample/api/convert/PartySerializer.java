package com.formssihk.cordaexample.api.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class PartySerializer extends JsonSerializer<Party> {
    @Override
    public void serialize(Party value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        CordaX500Name partialName = new CordaX500Name(
                null,
                value.getName().getOrganisationUnit(),
                value.getName().getOrganisation(),
                value.getName().getLocality(),
                value.getName().getState(),
                value.getName().getCountry()
                );
        gen.writeString(partialName.toString());
    }

}
