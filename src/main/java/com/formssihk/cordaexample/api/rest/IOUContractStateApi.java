package com.formssihk.cordaexample.api.rest;


import com.formssihk.cordaexample.api.payload.CreatePayload;
import com.formssihk.cordaexample.api.payload.Loan;
import com.formssihk.cordaexample.api.payload.UpdatePayload;
import com.formssihk.sample.state.IOUState;
import lombok.var;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.contracts.StateRef;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class IOUContractStateApi extends BaseStateRestApi<IOUState> {

    IOUContractStateApi(Class<IOUState> t) {
        super(t);
    }

    @PutMapping("create")
    private String create(@RequestBody CreatePayload obj) {
        return cordaService.createIOU(obj.getUserPublicKey(), Instant.now(), obj.getLoanRecords()).getTx().getId().toString();
    }

    @PutMapping("update/{ref}")
    private String update(@PathVariable("ref") StateRef ref, @RequestBody UpdatePayload obj) {
        StateAndRef<IOUState> iouState = cordaService.getState(ref, IOUState.class);
        if(!iouState.getState().getData().getUserPublicKey().equals(obj.getUserPublicKey())) throw new IllegalArgumentException("Public Key not match");
        if(obj.getLoanRecords().size()  == 0) throw new IllegalArgumentException("Loan Record cannot be empty");
        return cordaService.modifyIOU(ref, obj.getLoanRecords().stream().collect(Collectors.toMap(k -> {
            try {
                return k.sha256();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }, Loan::getStartDay))).getTx().getId().toString();
    }
}
