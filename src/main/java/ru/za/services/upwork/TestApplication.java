package ru.za.services.upwork;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.za.services.upwork.parser.Parser;
import ru.za.services.upwork.parser.SendEventData;
import ru.za.services.upwork.parser.SendEventListener;
import ru.za.services.upwork.parser.settings.ParserSettings;
import ru.za.services.upwork.transport.Mailer;
import ru.za.services.upwork.transport.TelegramApplication;


import java.util.Timer;
import java.util.TimerTask;

public class TestApplication {
    private static TestApplication ourInstance = new TestApplication();

    private Logger logger = LogManager.getLogger(TestApplication.class.getName());

    private Status status = Status.NOT_ACTIVE;
    private Timer mainTimer;
    private MainTimerTask mainTimerTask;
    private ParserSettings parserSettings = ParserSettings.getInstance();
    private Parser parser;
    private TelegramApplication telegramApplication = TelegramApplication.getInstance();

    /*
    private TestApplication() {
        logger.info("TestApplication constructor...");
        if (ourInstance != null){
            ourInstance.logger = logger;
            ourInstance.status = Status.NOT_ACTIVE;
        }else {
            this.logger = logger;
            this.status = Status.NOT_ACTIVE;
            ourInstance = this;
        }
        logger.info(this.toString());
    }
    */

    private TestApplication(){
        telegramApplication.startBot();
        //mainTimer = new Timer();
        //mainTimerTask = new MainTimerTask();
    }

    public void startParsing(){
        if (parser == null){
            parser = new Parser();

            parser.addEventListener(new EmailSender());
            parser.addEventListener(new TelegramSender());

            parser.doParse();
        }else{
            parser.doParse();
        }
    }

    public Status start(){
        parser = new Parser();

        parser.addEventListener(new EmailSender());
        parser.addEventListener(new TelegramSender());

        mainTimer = new Timer();
        mainTimerTask = new MainTimerTask();

        mainTimer.schedule(mainTimerTask, 1000, 600 *1000);

        return status = Status.ACTIVE;
    }

    public Status stop(){
        if (mainTimer != null){
            mainTimer.cancel();
            mainTimer = null;

            mainTimerTask.cancel();
            mainTimerTask = null;

            parser = null;
        }

        return status = Status.NOT_ACTIVE;
    }


    private class MainTimerTask extends TimerTask {
        public void run() {
            logger.info("Task started.");

            parser.doParse();

            logger.info("Task ended.");
        }

    }


    public static TestApplication getInstance() {
        return ourInstance;
    }

    public Status getStatus() {
        return status;
    }

    private class EmailSender implements SendEventListener {
        Logger logger = LogManager.getLogger(this.getClass().getName());
        @Override
        public void actionPerformed(SendEventData event) {
            if (event.getUserSettings().isSendToEmail()) {
                logger.info(String.format("User: %s, send message to e-mail", event.getUserSettings().getEmail()));
                Mailer.sendMail(event.getUserSettings().getEmail(), event.getMessage().getTitle(), event.getMessage().getDescription(), event.getMessage().getLink(), event.getLevel().toString(), event.getTechInfo());
            }
        }
    }

    private class TelegramSender implements SendEventListener{
        Logger logger = LogManager.getLogger(this.getClass().getName());
        @Override
        public void actionPerformed(SendEventData event) {
            if (event.getUserSettings().getTelegramId() != null) {
                logger.info(String.format("User: %s, send message to telegram", event.getUserSettings().getEmail()));

                String message = String.format("%s\n<a href=\"%s\">%s</a>\n%s", event.getLevel().toString(), event.getMessage().getLink(), event.getMessage().getTitle(), event.getMessage().getDescription());

                TelegramApplication app = TelegramApplication.getInstance();
                app.sendMessage(event.getUserSettings().getTelegramId(), message);
            }
        }
    }



}
