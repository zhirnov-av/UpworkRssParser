package ru.za.services.upwork;

import org.json.JSONObject;
import ru.za.services.upwork.parser.Parser;
import ru.za.services.upwork.parser.RssUrl;
import ru.za.services.upwork.parser.UserSettings;
import ru.za.services.upwork.rss.Feed;
import ru.za.services.upwork.rss.RSSFeedParser;

import java.io.File;
import java.util.Date;

public class TestMain {

    public static void main(String[] args){



//        UserSettings user = new UserSettings();
//
//        user.setEmail("zhirnov-av@yandex.ru");
//
//        user.getRssUrls().add(new RssUrl("1", "123", new Date(0)));
//        user.getRssUrls().add(new RssUrl("2", "234", new Date(0)));
//        user.getRssUrls().add(new RssUrl("3", "345", new Date(0)));
//
//        user.getNeededWords().add("(.+JAVA[^S].+)");
//        user.getNeededWords().add("(.+WEBSERVICE.+)");
//        user.getNeededWords().add("(.+WEB[\\s\\-]SERVICE.+)");
//        user.getNeededWords().add("(.+C#.+)");
//
//        String str = user.toString();
//
//        JSONObject obj = new JSONObject(str);
//        user = new UserSettings(obj);
//
//        System.out.println(user);

        RSSFeedParser feedParser = new RSSFeedParser(new File("c:\\test\\rss.xml"));
        try {
            Feed feed = feedParser.readFeed();
            System.out.println(feed.toString());

            for(int i = 0; i < feed.getMessages().size(); i++){
                System.out.println("******************************************\n");
                System.out.println(feed.getMessages().get(i).toString());
                System.out.println("******************************************\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
