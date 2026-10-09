package org.example.sdt_project.factory;

import org.example.sdt_project.domain.Account;

public interface AccountFactory
{
    Account createAccount(String customerId, double initialDeposit);
    String getType();
}

