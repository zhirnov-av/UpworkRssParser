package ru.za.services.upwork.transport.telegram.bot;

import ru.za.services.upwork.parser.SendEventData;

import java.util.EventListener;

public interface TelegramEventListener extends EventListener {
    public void actionPerformed(BotEvent event);
}
