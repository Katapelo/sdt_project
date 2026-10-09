package org.example.sdt_project.service;

import lombok.Getter;
import org.example.sdt_project.command.TransactionCommand;
import org.example.sdt_project.exception.BankingException;
import org.example.sdt_project.observer.TransactionCompletedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionManager
{
    private final ApplicationEventPublisher eventPublisher;

    @Getter
    private final List<TransactionCommand> history = new ArrayList<>();

    public TransactionManager(ApplicationEventPublisher eventPublisher)
    {
        this.eventPublisher = eventPublisher;
    }

    public void executeCommand(TransactionCommand command)
    {
        try
        {
            command.execute();
        }
        catch (BankingException ignored)
        {
         // rejection is handled by the command state
        }
        finally
        {
            history.add(command);
            eventPublisher.publishEvent(new TransactionCompletedEvent(this, command));
        }
    }

}
