package ru.za.services.upwork.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.za.services.upwork.parser.settings.ParserSettings;
import ru.za.services.upwork.parser.settings.UserSettings;
import ru.za.services.upwork.rss.Feed;
import ru.za.services.upwork.rss.RSSFeedParser;

import javax.xml.stream.XMLStreamException;

public class Parser {
    private ParserSettings settings = ParserSettings.getInstance();
    private Logger logger = LogManager.getLogger(this.getClass().getName());

    public void doParse(){
        for (UserSettings userSettings:settings.getUsersSettings()) {
            for (int i = 0; i < userSettings.getRssUrls().size(); i++){
                RSSFeedParser rssFeedParser = new RSSFeedParser(userSettings.getRssUrls().get(i).getUrl());
                try {
                    Feed feed = rssFeedParser.readFeed();
                    if (feed != null) {
                        UpworkFeed upworkFeed = new UpworkFeed(userSettings, userSettings.getRssUrls().get(i).getId(), feed, userSettings.getRssUrls().get(i).getLastScan());
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
        }
    }
}
