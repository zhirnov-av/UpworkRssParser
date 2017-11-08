package ru.za.services.upwork.transport.telegram.bot;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.HashSet;
import java.util.Set;

public class SenderBot extends TelegramLongPollingBot {

    private Set<TelegramEventListener> eventListeners = new HashSet<>();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().toUpperCase().contains("/USERLIST")){
                callListeners(new BotEvent(BotCommand.LIST_USERS, null));
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "Subchik";
    }

    @Override
    public String getBotToken() {
        return "473405530:AAEPQM5ctz_jAfj1xF5nXNSBUJlchl7hvbk";
    }

    public void addEventListener(TelegramEventListener listener){
        eventListeners.add(listener);
    }

    public void callListeners(BotEvent event){
        for (TelegramEventListener listener: eventListeners) {
            listener.actionPerformed(event);
        }
    }


    /*
    @Override
    public void onClosing() {

    }
    */
}
