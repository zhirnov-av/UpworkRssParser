package ru.za.services.upwork.transport.telegram.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.za.services.upwork.transport.telegram.bot.commands.*;

import java.util.HashSet;
import java.util.Set;

//CommandBot
public class SenderBot extends TelegramLongPollingCommandBot {

    public SenderBot(DefaultBotOptions options){
        super(options, "@Subchik");
        registerCommands();
    }

    public SenderBot(){
        super("@Subchik");
        registerCommands();
    }

    public void registerCommands(){
        register(new UserListCommand());
        register(new SubscribeCommand());
        register(new UnsubscribeCommand());
        register(new StartParsingCommand());
        register(new StopParsingCommand());
        register(new ChangeLevelCommand());
        register(new SelectLevelCommand());
        register(new ParseNowCommand());
    }

    /*
    public SenderBot(String botUsername) {
        super();
        //super("@Subchik2Bot");
        //register(new UserListCommand());
    }
    */



    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {
                SendMessage echoMessage = new SendMessage();
                echoMessage.setChatId(message.getChatId());
                echoMessage.setText("Hey heres your message:\n" + message.getText());

                try {
                    execute(echoMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        if (update.hasCallbackQuery()){
            CallbackQuery query = update.getCallbackQuery();

            if (query.getData().contains("/subscribe")){
                String[] split = query.getData().split(" ");
                if (split[1] != null){
                    SubscribeCommand command = new SubscribeCommand();
                    command.execute(this, query.getFrom(), query.getMessage().getChat(), new String[]{split[1]});
                }
            }

            if (query.getData().contains("/level")){
                String[] split = query.getData().split(" ");
                if (split[1] != null && split[2] != null){
                    ChangeLevelCommand command = new ChangeLevelCommand();
                    command.execute(this, query.getFrom(), query.getMessage().getChat(), new String[]{split[1], split[2]});
                }
            }

            /*
            SendMessage echoMessage = new SendMessage();
            echoMessage.setChatId(query.getFrom().getId().toString());
            echoMessage.setText("/subscribe " + query.getData());


            try {
                execute(echoMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            */
        }

    }



    /*
    @Override
    public String getBotUsername() {
        return "@Subchik2Bot";
    }
    */

    /*
    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().hasText()) {
            SendMessage echoMessage = new SendMessage();
            echoMessage.setChatId(update.getMessage().getChatId());
            echoMessage.setText("Hey heres your message:\n" + update.getMessage().getText());

            try {
                execute(echoMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
    */

    @Override
    public String getBotToken() {
        return "473405530:AAEPQM5ctz_jAfj1xF5nXNSBUJlchl7hvbk";
             //"462910253:AAF27l3KqWZl-vve7ZsTQQRpplDPEJiLnl8"
    }



    /*
    @Override
    public void onClosing() {

    }
    */
}
