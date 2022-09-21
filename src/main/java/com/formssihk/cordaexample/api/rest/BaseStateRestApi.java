package com.formssihk.cordaexample.api.rest;

import com.formssihk.cordaexample.api.convert.StateRefDeserializer;
import com.formssihk.cordaexample.service.CordaService;
import io.swagger.annotations.ApiOperation;
import kotlin.reflect.KClass;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.StateAndRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseStateRestApi<T> {

    private final Class<? extends T> t;

    BaseStateRestApi(Class<T> t) {
        super();
        this.t = t;
    }

    @Autowired
    CordaService cordaService;

    @GetMapping("ref/{ref}")
    public StateAndRef getByRef(@PathVariable("ref") String ref) {
        return cordaService.getState(StateRefDeserializer.parseString(ref), t.class);
    }

    @GetMapping("ref/{ref}/history")
    @ApiOperation("Returns all versions of a state identified by {ref}, including newer versions")
    public StateAndRef getHistoryByRef(@PathVariable("ref") String ref) {
        StateAndRef state = cordaService.getState(StateRefDeserializer.parseString(ref), t);
        return cordaService.getAllStatesByLinearId(state.getState().getData())
    }
}
