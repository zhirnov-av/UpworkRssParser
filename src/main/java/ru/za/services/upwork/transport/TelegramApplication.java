package ru.za.services.upwork.transport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.generics.BotSession;
import ru.za.services.upwork.transport.telegram.bot.SenderBot;

public class TelegramApplication {
    private BotSession botSession;
    private SenderBot bot;
    private Logger logger =  LogManager.getLogger(TelegramApplication.class.getName());

    private static TelegramApplication ourInstance = new TelegramApplication();

    private TelegramApplication(){

    }


    public void startBot(){
        /*
        HttpHost proxy = new HttpHost("10.2.3.10", 8080);
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        DefaultBotOptions options = new DefaultBotOptions();
        DefaultBotSession session = new DefaultBotSession();

        options.setRequestConfig(config);
        session.setOptions(options);
        */

        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        bot = new SenderBot();

        /*
        bot.getOptions().setRequestConfig(config);
        */

        try {

            botSession = botsApi.registerBot(bot);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void stopBot(){
        if (botSession != null && botSession.isRunning()) {
            botSession.stop();
        }
    }

    public void sendMessage(String chatId, String message){

        message = message.replace("<br>", "\n");
        message = message.replace("<br />", "\n");

        SendMessage echoMessage = new SendMessage();
        echoMessage.setChatId(chatId);
        echoMessage.setText(message);
        echoMessage.enableHtml(true);
        echoMessage.setParseMode(ParseMode.HTML);
        echoMessage.enableWebPagePreview();
        try {
            bot.execute(echoMessage);
        } catch (TelegramApiException e) {
            Logger logger = LogManager.getLogger(this.getClass().getName());
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static TelegramApplication getInstance(){
        return ourInstance;
    }
}
