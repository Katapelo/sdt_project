package org.example.sdt_project.observer;

import lombok.Getter;
import org.example.sdt_project.command.TransactionCommand;
import org.springframework.context.ApplicationEvent;

public class TransactionCompletedEvent extends ApplicationEvent
{
    @Getter
    private final TransactionCommand command;

    public TransactionCompletedEvent(Object source, TransactionCommand command)
    {
        super(source);
        this.command = command;
    }
}
