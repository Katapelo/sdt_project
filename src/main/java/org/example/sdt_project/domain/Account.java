package org.example.sdt_project.domain;

import lombok.Getter;
import lombok.Setter;
import org.example.sdt_project.domain.state.AccountState;
import org.example.sdt_project.domain.state.ActiveState;
import org.example.sdt_project.domain.state.FrozenState;

public class Account
{
    @Getter
    private final String accountId;

    @Getter
    @Setter
    private double balance;

    @Getter
    private AccountState state;

    public Account(String accountId, double balance)
    {
        this.accountId = accountId;
        this.balance = balance;
        this.state = new ActiveState();
    }

    public void withdraw(double amount)
    {
        state.withdraw(this, amount);
    }

    public void deposit(double amount)
    {
        state.deposit(this, amount);
    }

    public void freeze()
    {
        this.state = new FrozenState();
    }

    public void activate()
    {
        this.state = new ActiveState();
    }
}
