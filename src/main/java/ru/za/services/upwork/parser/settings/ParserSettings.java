package ru.za.services.upwork.parser.settings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParserSettings {
    private static ParserSettings ourInstance = new ParserSettings();

    private Logger logger = LogManager.getLogger(ParserSettings.class.getName());
    private String configFileName = "parser-settings.properties";

    private List<UserSettings> usersSettings = new ArrayList<UserSettings>();

    private String emailPassword;

    //private ArrayList<RssUrl> rssUrls = new ArrayList<>();
    //private ArrayList<String> neededWords = new ArrayList<>();


    public static ParserSettings getInstance() {
        return ourInstance;
    }

    private ParserSettings() {

        logger.info("ParserSettings constructor");
        readConfig();

        //rssUrls.add(new RssUrl("1", "https://www.upwork.com/ab/feed/jobs/rss?subcategory2=desktop_software_development&sort=renew_time_int+desc&api_params=1&q=&securityToken=c636015b813452f4796e847b381c87e69c9ca269c683f5e832e076f548ad3f8ee69bc2ec44d1b258dbdfea73c7fd6ea3489459d8d6dbe2f794a491f094b57b5a&userUid=819407936049897472&orgUid=819407936054091777", new Date(0)));
        //rssUrls.add(new RssUrl("2", "https://www.upwork.com/ab/feed/jobs/rss?subcategory2=other_software_development&sort=renew_time_int+desc&api_params=1&q=&securityToken=c636015b813452f4796e847b381c87e69c9ca269c683f5e832e076f548ad3f8ee69bc2ec44d1b258dbdfea73c7fd6ea3489459d8d6dbe2f794a491f094b57b5a&userUid=819407936049897472&orgUid=819407936054091777", new Date(0)));
        //rssUrls.add(new RssUrl("3", "https://www.upwork.com/ab/feed/jobs/rss?subcategory2=scripts_utilities&sort=renew_time_int+desc&api_params=1&q=&securityToken=c636015b813452f4796e847b381c87e69c9ca269c683f5e832e076f548ad3f8ee69bc2ec44d1b258dbdfea73c7fd6ea3489459d8d6dbe2f794a491f094b57b5a&userUid=819407936049897472&orgUid=819407936054091777", new Date(0)));

        //neededWords.add("(.+JAVA[^S].+)");
        //neededWords.add("(.+WEBSERVICE.+)");
        //neededWords.add("(.+WEB[\\s\\-]SERVICE.+)");
        //neededWords.add("(.+C#.+)");

        //writeConfig();

    }

    public void readConfig(){
        BufferedReader br = null;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        br = new BufferedReader( new InputStreamReader(classLoader.getResourceAsStream("settings.properties")));
        String strLine;

        try {
            strLine = br.readLine();
            String arr[] = strLine.split("=");
            if (arr[0].equals("email-password")){
                this.emailPassword = arr[1];
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null){
                    br.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        br = null;
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(configFileName);
            br = new BufferedReader( new InputStreamReader(fstream));

            while ((strLine = br.readLine()) != null){
                usersSettings.add(new UserSettings(new JSONObject(strLine)));
            }

            br.close();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (fstream != null){
                    fstream.close();
                }
                if (br != null){
                    br.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }



    }


    public void writeConfig(){
        try {
            FileOutputStream fstream = new FileOutputStream(configFileName);
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(fstream));
            try {
                for(int i = 0; i < usersSettings.size(); i++){
                    wr.write(usersSettings.get(i).toString());
                    wr.newLine();
                }
                wr.flush();
                wr.close();

                fstream.flush();
                fstream.close();
            } catch (IOException ex) {
                logger.error(ex);
            }
        } catch (FileNotFoundException ex) {
            logger.error(ex);
        }

    }

    public List<UserSettings> getUsersSettings() {
        return usersSettings;
    }

    public String getEmailPassword() {
        return emailPassword;
    }
}
