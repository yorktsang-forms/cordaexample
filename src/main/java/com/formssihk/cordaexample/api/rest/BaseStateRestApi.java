package com.formssihk.cordaexample.api.rest;

import com.formssihk.cordaexample.api.convert.StateRefDeserializer;
import com.formssihk.cordaexample.service.CordaService;
import io.swagger.annotations.ApiOperation;
import kotlin.reflect.KClass;
import lombok.var;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.contracts.UniqueIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class BaseStateRestApi<T extends LinearState> {

    private final Class<T> t;
    BaseStateRestApi(Class<T> t) {
        super();
        this.t = t;
    }

    @Autowired
    CordaService cordaService;

    @GetMapping("ref/{ref}")
    public StateAndRef<T> getByRef(@PathVariable("ref") String ref) {
        return cordaService.getState(StateRefDeserializer.parseString(ref), t);
    }

    @GetMapping("ref/{ref}/history")
    @ApiOperation("Returns all versions of a state identified by {ref}, including newer versions")
    public List<StateAndRef<T>> getHistoryByRef(@PathVariable("ref") String ref) {
        StateAndRef<T> state = cordaService.getState(StateRefDeserializer.parseString(ref), t);
        return cordaService.getAllStatesByLinearId(state.getState().getData().getLinearId(), t);
    }

    @GetMapping("ref/{ref}/history/latest")
    @ApiOperation("Returns latest version of a state identified by {ref}, including newer versions")
    public StateAndRef<T> getLatestFromHistoryByRef(@PathVariable String ref) {
        StateAndRef<T> state = cordaService.getState(StateRefDeserializer.parseString(ref), t);
        return cordaService.getLatestStateByLinearId(state.getState().getData().getLinearId(), t);
    }

    @GetMapping("linearId/{id}/latest")
    @ApiOperation("Returns the latest version of a state identified by its linearId.")
    StateAndRef<T> getByLinearId(@PathVariable("id") String id) {
        return cordaService.getStateByLinearId(new UniqueIdentifier(null, UUID.fromString(id)), t);
    }

    @GetMapping("linearId/{id}")
    @ApiOperation("Returns the latest version of a state identified by its linearId.")
    List<StateAndRef<T>> getAllByLinearId(@PathVariable("id") String id) {
        return cordaService.getAllStatesByLinearId(new UniqueIdentifier(null, UUID.fromString(id)), t);
    }

    @GetMapping("all")
    @ApiOperation("Returns all states (latest versions only).")
    List<StateAndRef<T>> all() {
        return cordaService.getAllState(t);
    }


}
