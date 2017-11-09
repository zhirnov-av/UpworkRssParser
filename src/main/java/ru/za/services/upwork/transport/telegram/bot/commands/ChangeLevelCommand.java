package ru.za.services.upwork.transport.telegram.bot.commands;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.za.services.upwork.parser.ImportanceLevel;
import ru.za.services.upwork.parser.settings.ParserSettings;
import ru.za.services.upwork.parser.settings.UserSettings;

public class ChangeLevelCommand extends BotCommand {

    public ChangeLevelCommand(){
        super("changelevel", "Subscribe to parser");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if (strings[0] != null && strings[1] != null) {

            boolean flagFound = false;

            ParserSettings parserSettings = ParserSettings.getInstance();
            for (UserSettings settings: parserSettings.getUsersSettings()) {
                if (settings.getEmail().equals(strings[0])){
                    settings.setMinimumLevel(ImportanceLevel.fromInt(Integer.parseInt(strings[1])));
                    flagFound = true;
                }
            }

            SendMessage answer = new SendMessage();
            answer.setChatId(chat.getId().toString());
            answer.enableMarkdown(true);
            answer.enableWebPagePreview();

            if (!flagFound){
                answer.setText(String.format("Can't found this user", strings[0]));
            }else{
                answer.setText(String.format("Min parsing level for %s is %s", strings[0], ImportanceLevel.fromInt(Integer.parseInt(strings[1])).toString()));
            }

            try {
                //absSender.sendMessage(answer);
                absSender.execute(answer);
            } catch (TelegramApiException e) {
            }
        }
    }
}
