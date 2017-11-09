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
import ru.za.services.upwork.TestApplication;
import ru.za.services.upwork.parser.settings.ParserSettings;

import java.util.ArrayList;
import java.util.List;

public class StartParsingCommand extends BotCommand {

    public StartParsingCommand(){
        super("start", "Start scheduled parsing");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        try {
            TestApplication application = TestApplication.getInstance();
            application.start();

            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

            SendMessage answer = new SendMessage();
            answer.setChatId(chat.getId().toString());
            answer.setReplyMarkup(keyboardMarkup);

            answer.enableMarkdown(true);
            answer.enableWebPagePreview();
            answer.setText("Started");

            try {
                absSender.execute(answer);
            } catch (TelegramApiException e) {
                Logger logger = LogManager.getLogger(this.getClass().getName());
                logger.error(e.getMessage());
            }
        }catch (Exception e){
            Logger logger = LogManager.getLogger(this.getClass().getName());
            logger.error(e.getMessage());
        }
    }
}
