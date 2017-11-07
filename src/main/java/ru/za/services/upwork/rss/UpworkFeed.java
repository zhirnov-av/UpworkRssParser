package ru.za.services.upwork.rss;

import org.apache.commons.mail.EmailException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.za.services.upwork.parser.KeywordsList;
import ru.za.services.upwork.parser.ParserSettings;
import ru.za.services.upwork.parser.UserSettings;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpworkFeed {
    Feed feed;
    Date lastDate;
    String id;
    UserSettings settings;

    Logger logger =  LogManager.getLogger(UpworkFeed.class.getName());

    public UpworkFeed(UserSettings settings, String id, Feed feed, Date lastDate){
        this.settings = settings;
        this.feed = feed;
        this.id = id;
        this.lastDate = lastDate;
    }

    public void processMessages(){
        for(int i = feed.getMessages().size() - 1; i >= 0; i--){
            FeedMessage message = feed.getMessages().get(i);
            if (message.getDate().after(lastDate)) {
                lastDate = message.getDate();

                StringBuilder regExBuilder = new StringBuilder();

                if (settings.getRegEx().isEmpty()) {

                    StringBuilder list = new StringBuilder("");
                    double summaryWeight = 0;
                    for(int j = 0; j < settings.getKeywords().size(); j++){
                        String regex = ".+[^S]" + settings.getKeywords().getKeyword(j).getKeyword().toUpperCase() + "[^S].+";

                        Pattern p = Pattern.compile(regex);
                        Matcher m = p.matcher(message.getDescription().toUpperCase());

                        if (m.find()){
                            summaryWeight += settings.getKeywords().getNormalizedWeight(j);

                            list.append(settings.getKeywords().getKeyword(j).getKeyword() + ";");

                            logger.info(String.format("keyword: %s, weight: %.02f, summary weight: %.03f",
                                    settings.getKeywords().getKeyword(j).getKeyword(),
                                    settings.getKeywords().getNormalizedWeight(j),
                                    summaryWeight));

                            //list.addKeyword(settings.getKeywords().getKeyword(j).getKeyword(), );
                        }
                    }

                    String techInfo = String.format("<br/><br/><br/><font size=\"2\">Keywords: %s<br/>Summary weight: %.02f</font>", list.toString(), summaryWeight);

                    if (summaryWeight >= 0.15){
                        Mailer.sendMail(settings.getEmail(), message.getTitle(), message.getDescription(), message.getLink(), "high", techInfo);
                        logger.info(String.format("Message sent to %s: keywords:[%s] weight: %.02f", settings.getEmail(), list.toString(), summaryWeight));
                    }else if(summaryWeight >= 0.06){
                        Mailer.sendMail(settings.getEmail(), message.getTitle(), message.getDescription(), message.getLink(), "medium", techInfo);
                        logger.info(String.format("Message sent to %s: keywords:[%s] weight: %.02f", settings.getEmail(), list.toString(), summaryWeight));
                    }else if(summaryWeight >= 0.02) {
                        Mailer.sendMail(settings.getEmail(), message.getTitle(), message.getDescription(), message.getLink(), "low", techInfo);
                        logger.info(String.format("Message sent to %s: keywords:[%s] weight: %.02f", settings.getEmail(), list.toString(), summaryWeight));
                    }else{
                        logger.info(String.format("Message does not sent: keywords:[%s] weight: %.02f", list.toString(), summaryWeight));
                    }


                    /*
                    for(int j = 0; j < list.size(); j++){
                        summaryWeight += list.getNormalizedWeight(j);
                        logger.info(String.format("keyword: %s, weight: %.02f, summary weight: %.03f", list.getKeyword(j).getKeyword(), list.getNormalizedWeight(j), summaryWeight));
                    }
                    if (summaryWeight >= 0.8){
                        logger.info(String.format("ID: %s, send message, rating: %s, title: %s", this.id, "high", message.getTitle()));
                        //Mailer.sendMail(settings.getEmail(), message.getTitle(), message.getDescription(), message.getLink(), "high");
                    }else if (summaryWeight >= 0.5){
                        logger.info(String.format("ID: %s, send message, rating: %s, title: %s", this.id, "medium", message.getTitle()));
                        //Mailer.sendMail(settings.getEmail(), message.getTitle(), message.getDescription(), message.getLink(), "medium");
                    }else if (summaryWeight >= 0.2){
                        logger.info(String.format("ID: %s, send message, rating: %s, title: %s", this.id, "low", message.getTitle()));
                        //Mailer.sendMail(settings.getEmail(), message.getTitle(), message.getDescription(), message.getLink(), "low");
                    } else {
                        logger.info(String.format("ID: %s, message no sent, rating: %s, title: %s", this.id, " < 0.2", message.getTitle()));
                    }
                    */



                    /*
                    for (int j = 0; j < settings.getNeededWords().size(); j++) {
                        if (j > 0)
                            regExBuilder.append("|");
                        regExBuilder.append(settings.getNeededWords().get(j));
                    }
                    */

                }else{
                    regExBuilder.append(settings.getRegEx());
                    Pattern p = Pattern.compile(regExBuilder.toString());
                    Matcher m = p.matcher(message.getDescription().toUpperCase());

                    if (m.find()){
                        //Mailer.sendMail(settings.getEmail(), message.getTitle(), message.getDescription(), message.getLink(), "regex");
                        logger.info(String.format("ID: %s, send message to %s Title: %s", this.id, settings.getEmail(), message.getTitle()));
                    }
                }

            }
        }
        for(int i = 0; i < settings.getRssUrls().size(); i++){
            if(settings.getRssUrls().get(i).getId().equals(this.id)){
                settings.getRssUrls().get(i).setLastScan(lastDate);
            }
        }
    }

    //public double checkKeywords(String )

    public Date getLastDate() {
        return lastDate;
    }

    public String getId() {
        return id;
    }
}
