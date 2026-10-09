package org.example.sdt_project.command;

import org.example.sdt_project.domain.Account;
import org.example.sdt_project.exception.BankingException;

public class TransferCommand implements TransactionCommand
{
    private final Account source;
    private final Account target;
    private final double amount;
    private String status = "PENDING";

    public TransferCommand(Account source, Account target, double amount)
    {
        this.source = source;
        this.target = target;
        this.amount = amount;
    }

    @Override
    public void execute()
    {
        try
        {
            source.withdraw(amount);
            target.deposit(amount);
            this.status = "SUCCESS";
        }
        catch (BankingException e)
        {
            this.status = "REJECTED - " + e.getMessage();
            throw e;
        }
    }

    @Override
    public String getStatus()
    {
        return status;
    }

    @Override
    public String getDetails()
    {
        return String.format("Transfer $%.2f from %s to %s", amount, source.getAccountId(), target.getAccountId());
    }
}
