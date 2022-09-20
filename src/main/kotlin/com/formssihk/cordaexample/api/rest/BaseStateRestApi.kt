package com.formssihk.cordaexample.api.rest

import com.formssihk.cordaexample.api.convert.StateRefDeserializer
import com.formssihk.cordaexample.service.CordaService
import com.formssihk.cordaexample.service.CordaService1
import io.swagger.annotations.ApiOperation
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.StateAndRef
import net.corda.core.contracts.UniqueIdentifier
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.*
import kotlin.reflect.KClass

open class BaseStateRestApi<T : LinearState>(val t: KClass<T>) {
    @Autowired
    protected lateinit var cordaService1: CordaService1

    @GetMapping("ref/{ref}")
    fun getByRef(@PathVariable("ref") ref: String): StateAndRef<T> {
        return cordaService1.getState(StateRefDeserializer.parseString(ref), t)
    }

    @GetMapping("ref/{ref}/history")
    @ApiOperation("Returns all versions of a state identified by {ref}, including newer versions")
    fun getHistoryByRef(@PathVariable ref: String): List<StateAndRef<T>> {
        val state = cordaService1.getState(StateRefDeserializer.parseString(ref), t)
        return cordaService1.getAllStatesByLinearId(state.state.data.linearId, t)
    }

    @GetMapping("ref/{ref}/history/latest")
    @ApiOperation("Returns latest version of a state identified by {ref}, including newer versions")
    fun getLatestFromHistoryByRef(@PathVariable ref: String): StateAndRef<T> {
        val state = cordaService1.getState(StateRefDeserializer.parseString(ref), t)
        return cordaService1.getLatestStateByLinearId(state.state.data.linearId, t)
    }

    @GetMapping("linearId/{id}/latest")
    @ApiOperation("Returns the latest version of a state identified by its linearId.")
    fun getByLinearId(@PathVariable("id") id: String): StateAndRef<T> {
        return cordaService1.getStateByLinearId(UniqueIdentifier(id = UUID.fromString(id)), t)
    }

    @GetMapping("linearId/{id}")
    @ApiOperation("Returns the latest version of a state identified by its linearId.")
    fun getAllByLinearId(@PathVariable("id") id: String): List<StateAndRef<T>> {
        return cordaService1.getAllStatesByLinearId(UniqueIdentifier(id = UUID.fromString(id)), t)
    }

    @GetMapping("all")
    @ApiOperation("Returns all states (latest versions only).")
    fun all(): List<StateAndRef<T>> {
        return cordaService1.getAllStates(t)
    }
}