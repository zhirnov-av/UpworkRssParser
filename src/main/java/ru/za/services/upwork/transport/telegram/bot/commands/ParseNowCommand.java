package ru.za.services.upwork.transport.telegram.bot.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.za.services.upwork.TestApplication;

public class ParseNowCommand extends BotCommand {

    public ParseNowCommand(){
        super("parse", "Start parsing");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        try {
            TestApplication application = TestApplication.getInstance();

            SendMessage answer = new SendMessage();
            answer.setChatId(chat.getId().toString());
            answer.setText("Try to start...");

            try {
                absSender.execute(answer);
            } catch (TelegramApiException e) {
                Logger logger = LogManager.getLogger(this.getClass().getName());
                logger.error(e.getMessage());
            }

            application.startParsing();

            answer.setText("Done.");
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
