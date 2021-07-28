package com.formssihk.cordaexample.api.rest

import com.formssihk.cordaexample.api.payload.CreatePayload
import com.formssihk.cordaexample.api.payload.UpdatePayload
import com.formssihk.sample.state.IOUState
import net.corda.core.contracts.StateRef
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException
import java.time.Instant

@RestController
@RequestMapping("api/v1/iou")
class IOUContractStateApi : BaseStateRestApi<IOUState>(IOUState::class) {

    @PutMapping("create")
    fun create(@RequestBody obj: CreatePayload) : String {
        return cordaService.createIOU(obj.userPublicKey, Instant.now(), obj.loadRecords ?: emptyMap()).tx.id.toString()
    }

    @PutMapping("update/{ref}")
    fun update(@PathVariable("ref") ref : StateRef, @RequestBody obj: UpdatePayload) : String {
        val iouState = cordaService.getState(ref, IOUState::class)
        if(iouState.state.data.userPublicKey != obj.userPublicKey) throw IllegalArgumentException("Public Key not match")
        if(obj.loadRecords.size == 0) throw IllegalArgumentException("Loan record cannot be empty")
        return cordaService.modifyIOU(ref, obj.loadRecords.associateBy( {it.sha256()}, {it.startDay})).tx.id.toString()
    }
}