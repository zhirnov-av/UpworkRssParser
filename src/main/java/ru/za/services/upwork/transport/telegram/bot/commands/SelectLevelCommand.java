package ru.za.services.upwork.transport.telegram.bot.commands;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiValidationException;
import ru.za.services.upwork.parser.ImportanceLevel;
import ru.za.services.upwork.parser.settings.ParserSettings;

import java.util.ArrayList;
import java.util.List;

public class SelectLevelCommand extends BotCommand {

    public SelectLevelCommand(){
        super("selectlevel", "Change minimum level");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        ParserSettings parserSettings = ParserSettings.getInstance();
        ImportanceLevel currentLevel = null;

        for(int i = 0; i < parserSettings.getUsersSettings().size(); i++){
            if (parserSettings.getUsersSettings().get(i).getEmail().equals(strings[0])){
                currentLevel = parserSettings.getUsersSettings().get(i).getMinimumLevel();
                break;
            }
        }

        if (currentLevel == null){
            SendMessage answer = new SendMessage();
            answer.setChatId(chat.getId().toString());
            answer.enableWebPagePreview();
            answer.setText(String.format("Can't find settings for user: %s", strings[0]));

            try {
                absSender.execute(answer);
            } catch (TelegramApiException e) {
                Logger logger = LogManager.getLogger(this.getClass().getName());
                logger.error(e.getMessage());
            }
            return;
        }

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for(int i = 0; i < ImportanceLevel.values().length; i++) {
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
            keyboardRow.add(new InlineKeyboardButton(ImportanceLevel.values()[i].toString())
                    .setCallbackData(String.format("/level %s %d", strings[0], ImportanceLevel.values()[i].valueAnInt())));
            keyboard.add(keyboardRow);
        }
        keyboardMarkup.setKeyboard(keyboard);

        try {
            keyboardMarkup.validate();
        } catch (TelegramApiValidationException e) {
            e.printStackTrace();
        }

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setReplyMarkup(keyboardMarkup);

        answer.enableMarkdown(true);
        answer.enableWebPagePreview();
        answer.setText(String.format("Current level: %s\nChoose new:", currentLevel.toString()));

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            Logger logger = LogManager.getLogger(this.getClass().getName());
            logger.error(e.getMessage());
        }
    }
}
