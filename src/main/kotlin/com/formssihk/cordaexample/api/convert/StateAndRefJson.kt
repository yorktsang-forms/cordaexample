package com.formssihk.cordaexample.api.convert

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import net.corda.core.contracts.ContractState
import net.corda.core.contracts.StateAndRef
import net.corda.core.contracts.StateRef
import net.corda.core.crypto.SecureHash
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class StateAndRefSerializer<T: ContractState>: JsonSerializer<StateAndRef<T>>() {
    override fun serialize(value: StateAndRef<T>, gen: JsonGenerator, serializers: SerializerProvider?) {
        val data = value.state.data
        val json = mutableMapOf(
            "ref" to value.ref,
            "_data" to data,
            "_type" to data::class.simpleName)
        gen.writeObject(json)
    }
}

 // probably not used
@JsonComponent
class StateAndRefDeserializer<T: ContractState>: JsonDeserializer<StateAndRef<T>>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): StateAndRef<T> {
        /*
        val obj = p.readValueAsTree<ObjectNode>()
        obj.get("ref")
        */
        return p.readValueAs(StateAndRef::class.java) as StateAndRef<T>
    }
}

// Corresponds to fields serialized above. Used for Springfox API documentation generation.
data class StateAndRefJsonModel<T>(
    val ref: StateRef,
    val _data: T,
    val _type: String
)

@JsonComponent
class StateRefSerializer: JsonSerializer<StateRef>() {
    override fun serialize(value: StateRef, gen: JsonGenerator, serializers: SerializerProvider?) {
        gen.writeString(value.toString())
    }
}

@JsonComponent
class StateRefDeserializer: JsonDeserializer<StateRef>() {
    companion object {
        fun parseString(str: String): StateRef {
            val parts = str.split('(', ')')
            return StateRef(SecureHash.parse(parts[0]), parts[1].toInt())
        }
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): StateRef {
        val str = p.readValueAs(String::class.java)
        return parseString(str)
    }
}
