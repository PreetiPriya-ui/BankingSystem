package com.system.banking.model;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public enum TransactionType {
    CREDIT(new BigDecimal(1)), DEBIT(new BigDecimal(-1));

    private final BigDecimal multiplicationFactor;

    public BigDecimal getMultiplicationFactor() {
        return multiplicationFactor;
    }
}
