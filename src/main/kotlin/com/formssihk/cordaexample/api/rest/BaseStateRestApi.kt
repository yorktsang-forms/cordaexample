package com.formssihk.cordaexample.api.rest

import com.formssihk.cordaexample.api.convert.StateRefDeserializer
import com.formssihk.cordaexample.service.CordaService
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
    protected lateinit var cordaService: CordaService

    @GetMapping("ref/{ref}")
    fun getByRef(@PathVariable("ref") ref: String): StateAndRef<T> {
        return cordaService.getState(StateRefDeserializer.parseString(ref), t)
    }

    @GetMapping("ref/{ref}/history")
    @ApiOperation("Returns all versions of a state identified by {ref}, including newer versions")
    fun getHistoryByRef(@PathVariable ref: String): List<StateAndRef<T>> {
        val state = cordaService.getState(StateRefDeserializer.parseString(ref), t)
        return cordaService.getAllStatesByLinearId(state.state.data.linearId, t)
    }

    @GetMapping("ref/{ref}/history/latest")
    @ApiOperation("Returns latest version of a state identified by {ref}, including newer versions")
    fun getLatestFromHistoryByRef(@PathVariable ref: String): StateAndRef<T> {
        val state = cordaService.getState(StateRefDeserializer.parseString(ref), t)
        return cordaService.getLatestStateByLinearId(state.state.data.linearId, t)
    }

    @GetMapping("linearId/{id}/latest")
    @ApiOperation("Returns the latest version of a state identified by its linearId.")
    fun getByLinearId(@PathVariable("id") id: String): StateAndRef<T> {
        return cordaService.getStateByLinearId(UniqueIdentifier(id = UUID.fromString(id)), t)
    }

    @GetMapping("linearId/{id}")
    @ApiOperation("Returns the latest version of a state identified by its linearId.")
    fun getAllByLinearId(@PathVariable("id") id: String): List<StateAndRef<T>> {
        return cordaService.getAllStatesByLinearId(UniqueIdentifier(id = UUID.fromString(id)), t)
    }

    @GetMapping("all")
    @ApiOperation("Returns all states (latest versions only).")
    fun all(): List<StateAndRef<T>> {
        return cordaService.getAllStates(t)
    }
}