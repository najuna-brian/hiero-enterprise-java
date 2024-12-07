package com.openelements.hiero.base.test;

import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.implementation.AccountClientImpl;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.protocol.AccountCreateResult;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountClientImplTest {

    private ProtocolLayerClient mockClient;
    private AccountClientImpl accountClient;

    @BeforeEach
    void setUp() {
        mockClient = Mockito.mock(ProtocolLayerClient.class);
        accountClient = new AccountClientImpl(mockClient);
    }

    @Test
    void createAccount_nullInitialBalance_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> accountClient.createAccount(null), "Expected NullPointerException for null initialBalance");
    }

    @Test
    void createAccount_zeroInitialBalance_createsAccountSuccessfully() throws HieroException {
        Hbar initialBalance = Hbar.fromTinybars(0);
        Account mockAccount = mock(Account.class);
        AccountCreateResult mockResult = mock(AccountCreateResult.class);

        when(mockClient.executeAccountCreateTransaction(any())).thenReturn(mockResult);
        when(mockResult.newAccount()).thenReturn(mockAccount);

        Account createdAccount = accountClient.createAccount(initialBalance);

        assertNotNull(createdAccount, "Account should be created successfully");
        verify(mockClient).executeAccountCreateTransaction(any());
    }

    @Test
    void createAccount_negativeInitialBalance_throwsIllegalArgumentException() {
        Hbar initialBalance = Hbar.fromTinybars(-100);
        assertThrows(IllegalArgumentException.class, () -> accountClient.createAccount(initialBalance), "Expected IllegalArgumentException for negative initialBalance");
    }

    @Test
    void createAccount_largeInitialBalance_createsAccountSuccessfully() throws HieroException {
        Hbar initialBalance = Hbar.fromTinybars(Long.MAX_VALUE);
        Account mockAccount = mock(Account.class);
        AccountCreateResult mockResult = mock(AccountCreateResult.class);

        when(mockClient.executeAccountCreateTransaction(any())).thenReturn(mockResult);
        when(mockResult.newAccount()).thenReturn(mockAccount);

        Account createdAccount = accountClient.createAccount(initialBalance);

        assertNotNull(createdAccount, "Account should be created successfully for large initialBalance");
        verify(mockClient).executeAccountCreateTransaction(any());
    }
}
