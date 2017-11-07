package ru.za.services.upwork.parser;

import java.util.ArrayList;

public class KeywordsList {
    private ArrayList<Keyword> list = new ArrayList<>();
    private int summaryWight = 0;

    public int size(){
        return list.size();
    }

    public void addKeyword(String keyword, int weight){
        list.add(new Keyword(keyword, weight));
        summaryWight += weight;
    }

    public Keyword getKeyword(int i){
        return list.get(i);
    }

    public void removeKeyword(int i){
        summaryWight -= list.get(i).getWeight();
        list.remove(i);

    }

    public double getNormalizedWeight(int index){
        return (double)list.get(index).getWeight()/(double)summaryWight;
    }

    public double getSummaryNormalizedWeight(){
        return ((double)summaryWight/100d)/(double)list.size();
    }


}
