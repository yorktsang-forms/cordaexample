package com.formssihk.cordaexample.api.payload;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;
@Data
public class CreatePayload {
    String userPublicKey;

    Map<String, Instant> loanRecords;

}

