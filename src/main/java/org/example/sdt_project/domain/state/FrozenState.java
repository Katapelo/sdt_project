package org.example.sdt_project.domain.state;

import org.example.sdt_project.domain.Account;
import org.example.sdt_project.exception.BankingException;

public class FrozenState implements AccountState
{

    @Override
    public void withdraw(Account account, double amount)
    {
        throw new BankingException("Account is FROZEN. Withdrawals/transfers blocked.");
    }

    @Override
    public void deposit(Account account, double amount)
    {
        account.setBalance(account.getBalance() + amount);
    }
}
