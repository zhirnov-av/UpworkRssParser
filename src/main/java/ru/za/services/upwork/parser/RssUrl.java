package ru.za.services.upwork.parser;

import org.json.JSONObject;

import java.util.Date;

public class RssUrl {
    private String id;
    private String url;
    private Date lastScan;

    public RssUrl(String id, String url, Date lastScan) {
        this.id = id;
        this.url = url;
        this.lastScan = lastScan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getLastScan() {
        return lastScan;
    }

    public void setLastScan(Date lastScan) {
        this.lastScan = lastScan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RssUrl rssUrl = (RssUrl) o;

        if (!id.equals(rssUrl.id)) return false;
        if (!url.equals(rssUrl.url)) return false;
        return lastScan.equals(rssUrl.lastScan);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + lastScan.hashCode();
        return result;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject(this);
        return json.toString();
    }
}
