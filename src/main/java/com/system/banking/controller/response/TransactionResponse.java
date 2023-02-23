package com.system.banking.controller.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Builder
public class TransactionResponse {

    private Long id;
    private String transactionType;
    private BigDecimal amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionResponse that = (TransactionResponse) o;
        return id.equals(that.id) && transactionType.equals(that.transactionType) && amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionType, amount);
    }
}
