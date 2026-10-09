package org.example.sdt_project.domain.state;

import org.example.sdt_project.domain.Account;
import org.example.sdt_project.exception.BankingException;

public class ActiveState implements AccountState
{
    @Override
    public void withdraw(Account account, double amount)
    {
        if (account.getBalance() < amount)
        {
            throw new BankingException("Insufficient funds. Available: " + account.getBalance());
        }
        account.setBalance(account.getBalance() - amount);
    }

    @Override
    public void deposit(Account account, double amount)
    {
        account.setBalance(account.getBalance() + amount);
    }

}
