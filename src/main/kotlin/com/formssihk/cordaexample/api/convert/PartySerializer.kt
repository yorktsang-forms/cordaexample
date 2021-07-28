package com.formssihk.cordaexample.api.convert

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import net.corda.core.identity.Party
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class PartySerializer : JsonSerializer<Party>() {
    override fun serialize(value: Party, gen: JsonGenerator, serializers: SerializerProvider?) {
        val partialName = value.name.copy(commonName = null)
        gen.writeString(partialName.toString())
    }
}