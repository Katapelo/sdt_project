package org.example.sdt_project.factory;

import org.example.sdt_project.domain.Account;
import org.example.sdt_project.domain.CheckingAccount;
import org.springframework.stereotype.Component;

@Component
public class CheckingAccountFactory implements AccountFactory
{

    @Override
    public Account createAccount(String customerId, double initialDeposit)
    {
        return new CheckingAccount("CHK" + customerId + System.currentTimeMillis(), initialDeposit);
    }

    @Override
    public String getType()
    {
        return "CHECKINGS";
    }
}
