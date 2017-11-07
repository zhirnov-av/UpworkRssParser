package ru.za.services.upwork.rss;

import org.apache.commons.mail.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.za.services.upwork.parser.ParserSettings;

public class Mailer {

    private static Logger logger =  LogManager.getLogger(Mailer.class.getName());

    public static void sendMail(String emailAdress, String title, String body, String link, String weight, String list){

        ParserSettings settings = ParserSettings.getInstance();

        try {
            ImageHtmlEmail email = new ImageHtmlEmail();
            email.setHostName("smtp.yandex.ru");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("zhirnov-av@yandex.ru", settings.getEmailPassword()));
            email.setAuthentication("zhirnov-av@yandex.ru", settings.getEmailPassword());
            email.setFrom("zhirnov-av@yandex.ru", "Upwork RSS feeds parser");
            email.setSSLCheckServerIdentity(true);
            email.setSSLOnConnect(true);
            email.setSubject(String.format("[upwork-bot][%s] - %s", weight, title));
            email.setHtmlMsg(String.format("<a href=\"%s\">%s</a><br/><br/><br/>%s<br/><br/>%s", link, title, body, list) );
            email.addTo(emailAdress);
            email.send();
        } catch (EmailException e) {
            logger.error(e.getMessage());
        }

    }
}
