package org.example.sdt_project.command;

public interface TransactionCommand
{
    void execute();
    String getStatus();
    String getDetails();
}
