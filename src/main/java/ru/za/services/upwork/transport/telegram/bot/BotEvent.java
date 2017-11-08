package ru.za.services.upwork.transport.telegram.bot;

import java.util.HashMap;

public class BotEvent {
    private BotCommand command;
    private HashMap<String, String> args = new HashMap<>();

    public BotEvent(BotCommand command, HashMap<String, String> args) {
        this.command = command;
        this.args = args;
    }

    public HashMap<String, String>getArgs(){
        return new HashMap<>(args);
    }

    public BotCommand getCommand() {
        return command;
    }
}
