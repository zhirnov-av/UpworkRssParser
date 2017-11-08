package ru.za.services.upwork.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.za.services.upwork.parser.settings.ParserSettings;
import ru.za.services.upwork.parser.settings.UserSettings;
import ru.za.services.upwork.rss.Feed;
import ru.za.services.upwork.rss.RSSFeedParser;
import ru.za.services.upwork.transport.Mailer;

import javax.xml.stream.XMLStreamException;
import java.util.HashSet;
import java.util.Set;

public class Parser {
    private ParserSettings settings = ParserSettings.getInstance();
    private Logger logger = LogManager.getLogger(this.getClass().getName());
    private Set<SendEventListener> listeners = new HashSet<>();


    public void doParse(){

        logger.info("Parser start parsing...");

        for (UserSettings userSettings:settings.getUsersSettings()) {
            logger.info(String.format("parsing fo user %s...", userSettings.getEmail()));
            for (int i = 0; i < userSettings.getRssUrls().size(); i++){
                RSSFeedParser rssFeedParser = new RSSFeedParser(userSettings.getRssUrls().get(i).getUrl());
                try {
                    Feed feed = rssFeedParser.readFeed();
                    if (feed != null) {
                        UpworkFeed upworkFeed = new UpworkFeed(this, userSettings, userSettings.getRssUrls().get(i).getId(), feed, userSettings.getRssUrls().get(i).getLastScan());
                        upworkFeed.processMessages();
                    }else{
                        logger.info("ERROR feed is null");
                    }
                } catch (XMLStreamException e) {
                    logger.error(e.getMessage());
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
            logger.info(String.format("parsing fo user %s done.", userSettings.getEmail()));
        }
    }

    public void addEventListener(SendEventListener listener){
        listeners.add(listener);
    }

    public HashSet<SendEventListener> getListeners(){
        return new HashSet<>(listeners);
    }



}
