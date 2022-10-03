package com.atfx.sample.payload;


import kotlin.text.Charsets;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

@Data
@AllArgsConstructor
public class Loan {
    private String borrower;
    private String lender;
    private Double principal;
    private Instant startDay;

    public String sha256() throws NoSuchAlgorithmException {
        byte[] bytes = this.toString().getBytes(Charsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(bytes);

        //return digest.fold("", { str, it -> str + "%02x".format(it) })
        return "fuck";
    }
}
