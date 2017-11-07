package ru.za.services.upwork.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.za.services.upwork.parser.settings.UserSettings;
import ru.za.services.upwork.transport.Mailer;
import ru.za.services.upwork.rss.Feed;
import ru.za.services.upwork.rss.FeedMessage;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpworkFeed {
    Feed feed;
    Date lastDate;
    String id;
    UserSettings settings;

    Logger logger =  LogManager.getLogger(UpworkFeed.class.getName());

    private class CheckingResult{
        private ImportanceLevel level;
        private String keywordsList;
        private double summaryWeight;
        private String techInfo;

        public CheckingResult(ImportanceLevel level, String keywordsList, double summaryWeight, String techInfo) {
            this.level = level;
            this.keywordsList = keywordsList;
            this.summaryWeight = summaryWeight;
            this.techInfo = techInfo;
        }

        public ImportanceLevel getLevel() {
            return level;
        }

        public String getKeywordsList() {
            return keywordsList;
        }

        public double getSummaryWeight() {
            return summaryWeight;
        }

        public String getTechInfo() {
            return techInfo;
        }
    }

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

                    CheckingResult result = checkKeywords(message.getDescription().toUpperCase());

                    switch(result.getLevel()){
                        case HIGH:
                        case MEDIUM:
                        case LOW:
                        case TRASH:
                            Mailer.sendMail(settings.getEmail(), message.getTitle(), message.getDescription(), message.getLink(), result.getLevel().toString(), result.getTechInfo());
                            logger.info(String.format("Message sent to %s: %s weight: %.02f", settings.getEmail(), result.getKeywordsList(), result.getSummaryWeight()));
                            break;
                            //logger.info(String.format("Message does not sent: %s weight: %.02f", result.getKeywordsList(), result.getSummaryWeight()));
                    }

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

    public CheckingResult checkKeywords(String messageBody){
        StringBuilder list = new StringBuilder("Keywords[");
        double summaryWeight = 0;
        for(int j = 0; j < settings.getKeywords().size(); j++){
            String regex = ".+[^S]" + settings.getKeywords().getKeyword(j).getKeyword().toUpperCase() + "[^S].+";

            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(messageBody);

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
        list.append("] ");

        ImportanceLevel level;


        if (summaryWeight > 0) {
            list.append("Excepted keywords[");
            for (int j = 0; j < settings.getExceptKeywords().size(); j++) {
                String regex = ".+[^S]" + settings.getExceptKeywords().getKeyword(j).getKeyword().toUpperCase() + "[^S].+";

                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(messageBody);

                if (m.find()) {
                    summaryWeight -= settings.getExceptKeywords().getNormalizedWeight(j);

                    list.append(settings.getExceptKeywords().getKeyword(j).getKeyword() + ";");

                    logger.info(String.format("excepted keyword: %s, weight: %.02f, summary weight: %.03f",
                            settings.getExceptKeywords().getKeyword(j).getKeyword(),
                            settings.getExceptKeywords().getNormalizedWeight(j),
                            summaryWeight));
                }
            }
            list.append("]");
        }
        String techInfo = String.format("<br/><font size=\"2\">%s<br/>Summary weight: %.02f</font>", list.toString(), summaryWeight);

        if (summaryWeight >= 0.12){
            level = ImportanceLevel.HIGH;
        }else if(summaryWeight >= 0.06){
            level = ImportanceLevel.MEDIUM;
        }else if(summaryWeight >= 0.02) {
            level = ImportanceLevel.LOW;
        }else{
            level = ImportanceLevel.TRASH;
        }

        return new CheckingResult(level, list.toString(), summaryWeight, techInfo);
    }

    public Date getLastDate() {
        return lastDate;
    }

    public String getId() {
        return id;
    }
}
