package com.example.AndroidServer.model;

import java.util.Date;

public class ChargeHistory {
    private Date date;
    private Double amount;

    public ChargeHistory(Date date, Double amount) {
        this.date = date;
        this.amount = amount;
    }

    // Getter and Setter
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}