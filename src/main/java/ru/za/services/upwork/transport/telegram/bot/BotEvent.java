package ru.za.services.upwork.transport.telegram.bot;

import org.telegram.telegrambots.api.objects.Update;

import java.util.HashMap;

public class BotEvent {
    private BotCommand command;
    private HashMap<String, String> args = new HashMap<>();
    private Update update;

    public BotEvent(BotCommand command, HashMap<String, String> args, Update update) {
        this.command = command;
        this.args = args;
        this.update = update;
    }

    public HashMap<String, String>getArgs(){
        return new HashMap<>(args);
    }

    public BotCommand getCommand() {
        return command;
    }

    public Update getUpdate() {
        return update;
    }
}
