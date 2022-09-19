package com.formssihk.sample.payload;


import kotlin.text.Charsets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class Loan {
    private String borrower;
    private String lender;
    private Double principal;
    private Instant startDay;

    public Loan(String borrower, String lender, Double principal, Instant startDay) {
        this.borrower = borrower;
        this.lender = lender;
        this.principal = principal;
        this.startDay = startDay;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getLender() {
        return lender;
    }

    public void setLender(String lender) {
        this.lender = lender;
    }

    public Double getPrincipal() {
        return principal;
    }

    public void setPrincipal(Double principal) {
        this.principal = principal;
    }

    public Instant getStartDay() {
        return startDay;
    }

    public void setStartDay(Instant startDay) {
        this.startDay = startDay;
    }

    public String sha256() throws NoSuchAlgorithmException {
        byte[] bytes = this.toString().getBytes(Charsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(bytes);
        //return digest.fold("", { str, it -> str + "%02x".format(it) })
        return "fuck";
    }
}
