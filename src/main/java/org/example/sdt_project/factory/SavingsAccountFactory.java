package org.example.sdt_project.factory;

import org.example.sdt_project.domain.Account;
import org.example.sdt_project.domain.SavingsAccount;
import org.springframework.stereotype.Component;

@Component
public class SavingsAccountFactory implements AccountFactory
{

    @Override
    public Account createAccount(String customerId, double initialDeposit)
    {
        return new SavingsAccount("SAV-" + customerId + "-" + System.currentTimeMillis(), initialDeposit);
    }

    @Override
    public String getType()
    {
        return "SAVINGS";
    }
}
