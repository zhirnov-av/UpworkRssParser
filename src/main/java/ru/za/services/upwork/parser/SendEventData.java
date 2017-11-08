package ru.za.services.upwork.parser;

import ru.za.services.upwork.parser.settings.UserSettings;
import ru.za.services.upwork.rss.FeedMessage;

public class SendEventData {
    private UserSettings userSettings;
    private FeedMessage message;
    private ImportanceLevel level;
    private String techInfo;

    public SendEventData(UserSettings settings, FeedMessage message, ImportanceLevel level, String techInfo) {
        this.userSettings = settings;
        this.message = message;
        this.level = level;
        this.techInfo = techInfo;
    }

    public FeedMessage getMessage() {
        return message;
    }

    public ImportanceLevel getLevel() {
        return level;
    }

    public String getTechInfo() {
        return techInfo;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }
}
