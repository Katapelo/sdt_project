package org.example.sdt_project.domain.state;

import org.example.sdt_project.domain.Account;

public interface AccountState
{
    void withdraw(Account account, double amount);
    void deposit(Account account, double amount);
}

