package com.formssihk.cordaexample.api.payload;

import lombok.Data;

import java.util.List;

@Data
public class UpdatePayload {
    String userPublicKey;

    List<Loan> loanRecords;
}
