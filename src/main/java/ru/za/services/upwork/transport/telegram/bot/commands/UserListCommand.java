package ru.za.services.upwork.transport.telegram.bot.commands;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiValidationException;
import ru.za.services.upwork.parser.Parser;
import ru.za.services.upwork.parser.settings.ParserSettings;

import java.util.ArrayList;
import java.util.List;

public class UserListCommand extends BotCommand {

    public UserListCommand(){
        super("userlist", "");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        ParserSettings parserSettings = ParserSettings.getInstance();


        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for(int i = 0; i < parserSettings.getUsersSettings().size(); i++) {
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
            keyboardRow.add(new InlineKeyboardButton(parserSettings.getUsersSettings().get(i).getEmail())
                    .setCallbackData(String.format("/subscribe %s", parserSettings.getUsersSettings().get(i).getEmail()))
                    .setSwitchInlineQuery(String.format("%d", i)));
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
        answer.setText("Choose settings to subscribe");


        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            Logger logger = LogManager.getLogger(this.getClass().getName());
            logger.error(e.getMessage());
        }
    }
}
