package org.example.sdt_project.service;

import org.example.sdt_project.command.TransactionCommand;
import org.example.sdt_project.command.TransferCommand;
import org.example.sdt_project.domain.Account;
import org.example.sdt_project.factory.AccountFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TransactionManagerIntegrationTest
{
    @Autowired
    private TransactionManager transactionManager;

    @Autowired
    private List<AccountFactory> factories;

    private AccountFactory savingsFactory;
    private AccountFactory checkingFactory;

    @BeforeEach
    void setUp()
    {
        savingsFactory = factories.stream()
                .filter(f -> f.getType().equals("SAVINGS")).findFirst().orElseThrow();
        checkingFactory = factories.stream()
                .filter(f -> f.getType().equals("CHECKINGS")).findFirst().orElseThrow();
    }

    @Test
    void testSuccessfulTransfer()
    {
        Account alice = savingsFactory.createAccount("ALICE", 1000.0);
        Account paul = checkingFactory.createAccount("PAUL", 500.0);

        TransactionCommand transfer = new TransferCommand(alice, paul, 200.0);
        transactionManager.executeCommand(transfer);

        assertEquals(800.0, alice.getBalance(), "Alice should be debited 200");
        assertEquals(700.0, paul.getBalance(), "Paul should be credited 200");
        assertEquals("SUCCESS", transfer.getStatus());
    }

    @Test
    void testInsufficientFundsTransfer()
    {
        Account kayle = savingsFactory.createAccount("KAYLE", 100.0);
        Account david = checkingFactory.createAccount("DAVID", 0.0);

        TransactionCommand transfer = new TransferCommand(kayle, david, 500.0);
        transactionManager.executeCommand(transfer);

        assertEquals(100.0, kayle.getBalance(), "Kayle's balance should remain unchanged");
        assertEquals(0.0, david.getBalance(), "David's balanced should remain unchanged");
        assertTrue(transfer.getStatus().contains("REJECTED - Insufficient funds"));

    }

    @Test
    void testFrozenAccountTransfer()
    {
        Account evelyn = savingsFactory.createAccount("EVELYN", 5000.0);
        Account mallory = checkingFactory.createAccount("MALLORY", 0.0);

        evelyn.freeze();

        TransactionCommand transfer = new TransferCommand(evelyn, mallory, 1000.0);
        transactionManager.executeCommand(transfer);

        assertEquals(5000.0, evelyn.getBalance(), "Evelyn's account should be frozen");
        assertTrue(transfer.getStatus().contains("REJECTED - Account is FROZEN"));
    }
}
