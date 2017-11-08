package ru.za.services.upwork.transport;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.za.services.upwork.transport.telegram.bot.BotEvent;
import ru.za.services.upwork.transport.telegram.bot.SenderBot;
import ru.za.services.upwork.transport.telegram.bot.TelegramEventListener;

public class TelegramApplication {
    private BotSession botSession;
    private SenderBot bot;
    private Logger logger =  LogManager.getLogger(TelegramApplication.class.getName());

    public void startBot(){
        HttpHost proxy = new HttpHost("10.2.3.10", 8080);
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        DefaultBotOptions options = new DefaultBotOptions();
        //DefaultBotSession session = new DefaultBotSession();

        options.setRequestConfig(config);

        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        bot = new SenderBot();
        bot.getOptions().setRequestConfig(config);
        bot.addEventListener(new EventProcessor());
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

    private class EventProcessor implements TelegramEventListener{

        @Override
        public void actionPerformed(BotEvent event) {
            switch (event.getCommand()){
                case SUBSCRIBE:
                    logger.info("SUBSCRIBE command");
                    break;
                case LIST_USERS:
                    logger.info("LIST_USERS command");
                    break;
            }
        }
    }
}
