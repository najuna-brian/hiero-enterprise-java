package com.openelements.hedera.base.mirrornode;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

public record NetworkFee(long gas, @NonNull String transactionType) {
    public NetworkFee {
        Objects.requireNonNull(transactionType, "transactionType must not be null");
    }
}
