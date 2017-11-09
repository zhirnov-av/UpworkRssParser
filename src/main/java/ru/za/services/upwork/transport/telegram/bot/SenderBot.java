package ru.za.services.upwork.transport.telegram.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.za.services.upwork.transport.telegram.bot.commands.SubscribeCommand;
import ru.za.services.upwork.transport.telegram.bot.commands.UserListCommand;

import java.util.HashSet;
import java.util.Set;

//CommandBot
public class SenderBot extends TelegramLongPollingCommandBot {

    private Set<TelegramEventListener> eventListeners = new HashSet<>();

    public SenderBot(DefaultBotOptions options){
        super(options, "@Subchik2Bot");
        register(new UserListCommand());
        register(new SubscribeCommand());
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
        return "462910253:AAF27l3KqWZl-vve7ZsTQQRpplDPEJiLnl8";
             //"462910253:AAF27l3KqWZl-vve7ZsTQQRpplDPEJiLnl8"
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
