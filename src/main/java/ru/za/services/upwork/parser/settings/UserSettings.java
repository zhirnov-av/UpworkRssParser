package ru.za.services.upwork.parser.settings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.za.services.upwork.parser.*;
import ru.za.services.upwork.transport.Mailer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserSettings {
    private String email = "";
    private ArrayList<RssUrl> rssUrls = new ArrayList<>();
    @Deprecated
    private ArrayList<String> neededWords = new ArrayList<>();

    private KeywordsList keywords = new KeywordsList();
    private KeywordsList exceptKeywords = new KeywordsList();
    private boolean sendToEmail = true;
    private String telegramId = null;

    private ImportanceLevel minimumLevel = ImportanceLevel.MEDIUM;

    private String regEx = "";

    @Override
    public String toString() {
        JSONObject json = new JSONObject(this);
        return json.toString();
    }

    public KeywordsList getKeywords() {
        return keywords;
    }

    public KeywordsList getExceptKeywords() {
        return exceptKeywords;
    }

    public void setExceptKeywords(KeywordsList exceptKeywords) {
        this.exceptKeywords = exceptKeywords;
    }

    @Deprecated
    public void addKeyword(String keyword){
        if (keyword != null)
            neededWords.add(String.format("(.+%s.+)", keyword.toUpperCase()));
    }

    @Deprecated
    public void addKeywordRegex(String keyword){
        if (keyword != null)
            neededWords.add(String.format("(%s)", keyword));
    }

    public void addFeedUrl(String url){
        int index = rssUrls.size() + 1;
        rssUrls.add(new RssUrl(String.format("%d", index), url, new Date(0)));
    }

    public void removeFeedUrl(String id){
        for(int i = 0; i < rssUrls.size(); i++){
            if (rssUrls.get(i).getId().equals(id)){
                rssUrls.remove(i);
            }
        }
    }

    public String getRegEx() {
        return regEx;
    }

    public void setRegEx(String regEx) {
        this.regEx = regEx;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<RssUrl> getRssUrls() {
        return rssUrls;
    }

    @Deprecated
    public ArrayList<String> getNeededWords() {
        return neededWords;
    }

    public UserSettings() {
    }

    public UserSettings(JSONObject object) {
        JSONArray jsonArray = object.getJSONArray("rssUrls");
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);

        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject obj = (JSONObject) jsonArray.get(i);
            Date lastDate = null;
            try {
                lastDate = formatter.parse(obj.getString("lastScan"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            rssUrls.add(new RssUrl(obj.getString("id"), obj.getString("url"), lastDate));
        }

        jsonArray = object.getJSONArray("neededWords");
        for (int i = 0; i < jsonArray.length(); i++){
            neededWords.add((String)jsonArray.get(i));
        }

        /*
        jsonArray = object.getJSONArray("keywords");
        for (int i = 0; i < jsonArray.length(); i++){

        }
        */

        keywords.addKeyword("java", 100);
        keywords.addKeyword("simple", 70);
        keywords.addKeyword("console", 70);
        keywords.addKeyword("pdf", 100);
        keywords.addKeyword("itext", 100);
        keywords.addKeyword("pdfbox", 100);
        keywords.addKeyword("web-service", 100);
        keywords.addKeyword("web service", 100);
        keywords.addKeyword("webservice", 100);
        keywords.addKeyword("SOAP", 100);
        keywords.addKeyword("SQL", 100);
        keywords.addKeyword("CSV", 100);
        keywords.addKeyword("ORACLE", 100);
        keywords.addKeyword("C#", 30);
        keywords.addKeyword("C/C\\+\\+", 30);

        exceptKeywords.addKeyword("php", 50);
        exceptKeywords.addKeyword("javascript", 100);
        exceptKeywords.addKeyword("wordpress", 100);
        exceptKeywords.addKeyword("mining", 100);
        exceptKeywords.addKeyword("arduino", 100);
        exceptKeywords.addKeyword("blockchain", 100);
        exceptKeywords.addKeyword("bitcoin", 100);

        email = (String)object.get("email");
        try{
            regEx = (String)object.get("regEx");
        }catch (Exception e){
            regEx = null;
        }

        telegramId = (String)object.get("telegramId");
        try {
            minimumLevel = ImportanceLevel.fromString(object.get("minimumLevel").toString());
        }catch (Exception e){
            minimumLevel = ImportanceLevel.MEDIUM;
        }
    }

    public boolean isSendToEmail() {
        return sendToEmail;
    }


    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public String getTelegramId() {
        return telegramId;
    }

    public ImportanceLevel getMinimumLevel() {
        return minimumLevel;
    }

    public void setMinimumLevel(ImportanceLevel minimumLevel) {
        this.minimumLevel = minimumLevel;
    }
}

