package ru.za.services.upwork;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.za.services.upwork.parser.Parser;
import ru.za.services.upwork.parser.ParserSettings;
import ru.za.services.upwork.rss.Feed;
import ru.za.services.upwork.rss.RSSFeedParser;
import ru.za.services.upwork.rss.UpworkFeed;

import javax.annotation.Resource;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TestApplication {
    private static TestApplication ourInstance = new TestApplication();

    private Logger logger = LogManager.getLogger(TestApplication.class.getName());

    private Status status = Status.NOT_ACTIVE;
    private Timer mainTimer;
    private MainTimerTask mainTimerTask;
    private ParserSettings parserSettings = ParserSettings.getInstance();


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



    public Status start(){

        mainTimer = new Timer();
        mainTimerTask = new MainTimerTask();

        mainTimer.schedule(mainTimerTask, 1000, 600 *1000);

        return status = Status.ACTIVE;
    }

    public Status stop(){
        if (mainTimer != null){
            mainTimer.cancel();
            mainTimer.purge();
            mainTimer = null;
        }

        return status = Status.NOT_ACTIVE;
    }


    private class MainTimerTask extends TimerTask {
        public void run() {
            logger.info("Task started.");
            Parser parser = new Parser();
            parser.doParse();
            logger.info("Task ended.");
        }
        public void main(){

            /*
            Date maxDate = new Date(0);

            ArrayList<UpworkFeed> upworkFeeds = new ArrayList<>();

            try {
                RSSFeedParser parser = new RSSFeedParser("https://www.upwork.com/ab/feed/jobs/rss?subcategory2=desktop_software_development&sort=renew_time_int+desc&api_params=1&q=&securityToken=c636015b813452f4796e847b381c87e69c9ca269c683f5e832e076f548ad3f8ee69bc2ec44d1b258dbdfea73c7fd6ea3489459d8d6dbe2f794a491f094b57b5a&userUid=819407936049897472&orgUid=819407936054091777");
                Feed feed = parser.readFeed();

                UpworkFeed upworkFeed = new UpworkFeed("1", feed);
                upworkFeed.processMessages();
                upworkFeeds.add(upworkFeed);

                parser = new RSSFeedParser("https://www.upwork.com/ab/feed/jobs/rss?subcategory2=other_software_development&sort=renew_time_int+desc&api_params=1&q=&securityToken=c636015b813452f4796e847b381c87e69c9ca269c683f5e832e076f548ad3f8ee69bc2ec44d1b258dbdfea73c7fd6ea3489459d8d6dbe2f794a491f094b57b5a&userUid=819407936049897472&orgUid=819407936054091777");
                feed = parser.readFeed();

                upworkFeed = new UpworkFeed("2", feed);
                upworkFeed.processMessages();
                upworkFeeds.add(upworkFeed);

                parser = new RSSFeedParser("https://www.upwork.com/ab/feed/jobs/rss?subcategory2=scripts_utilities&sort=renew_time_int+desc&api_params=1&q=&securityToken=c636015b813452f4796e847b381c87e69c9ca269c683f5e832e076f548ad3f8ee69bc2ec44d1b258dbdfea73c7fd6ea3489459d8d6dbe2f794a491f094b57b5a&userUid=819407936049897472&orgUid=819407936054091777");
                feed = parser.readFeed();

                upworkFeed = new UpworkFeed("3", feed);
                upworkFeed.processMessages();
                upworkFeeds.add(upworkFeed);

            } catch (XMLStreamException e) {
                e.printStackTrace();
            }

            try {
                FileOutputStream fstream = new FileOutputStream("c:\\tmp\\config.cfg", false);
                BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(fstream));

                for (UpworkFeed feed:upworkFeeds){
                    wr.write(String.format("%s=%s", feed.getId(), feed.getLastDate().toString()));
                    wr.newLine();
                }
                wr.flush();

                fstream.flush();
                fstream.close();
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
        }

    }


    public static TestApplication getInstance() {
        return ourInstance;
    }

    public Status getStatus() {
        return status;
    }



}
