package ru.za.services.upwork.parser;

import org.json.JSONObject;

public class Keyword {
    private String keyword;
    private int weight;

    public Keyword(String keyword, int weight) {
        this.keyword = keyword;
        this.weight = weight;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject(this);
        return json.toString();
    }
}
