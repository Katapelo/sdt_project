package org.example.sdt_project.observer;

import lombok.extern.slf4j.Slf4j;
import org.example.sdt_project.command.TransactionCommand;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailNotificationObserver
{
    @Async
    @EventListener
    public void onTransactionCompleted(TransactionCompletedEvent event)
    {
        TransactionCommand cmd = event.getCommand();
        if("SUCCESS".equals(cmd.getStatus()))
        {
            log.info("Sending Email: Your transfer was successful.");
        }
        else
        {
            log.info("Sending Email: Your transfer failed. Reason: {}", cmd.getStatus());
        }
    }
}
