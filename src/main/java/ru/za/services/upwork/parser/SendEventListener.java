package ru.za.services.upwork.parser;

import java.util.EventListener;

public interface SendEventListener extends EventListener {
    public void actionPerformed(SendEventData event);
}
