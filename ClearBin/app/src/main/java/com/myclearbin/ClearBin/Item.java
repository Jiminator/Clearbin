package com.myclearbin.ClearBin;
import org.json.*;

import java.util.ArrayList;

public class Item {
    public ArrayList<Integer> category = new ArrayList<>();
    public int disposable;
    public String name;
    public String special;

    public int id;
    public String query;
    public String src;
    public boolean earthCompatible;
    public long searches;

    public Item(String name, int id, int disposable,  String special, boolean earthCompatible, long searches, String src, String query) {
        this.disposable = disposable;
        this.name = name;
        this.id = id;
        this.special = special;
        this.query = query;
        this.src = src;
        this.earthCompatible = earthCompatible;
        this.searches = searches;
    }

    public void addCategory(int id){
        category.add(id);
    }

    public void incrementSearch(){
        this.searches++;
    }

    @Override
    public String toString() {
        return "\nName: "+name+"\nId: "+id+"\nDisposable: "+disposable+"\nCategory: "+category+"\nSpecial: "+special+"\nEarthCompatible: "+earthCompatible+"\nSearches: "+searches+"\nSrc: "+src+"\nQuery: "+query;
    }

    public JSONObject toJSONObject() throws Exception{
        return new JSONObject().put("Name",name).put("Id",id).put("Disposable",disposable).put("Category",category).put("Special",special).put("EarthCompatible",earthCompatible).put("Searches",searches).put("Src",src).put("Query",query);
    }

    public String q (String s){
        return "\"" + s + "\"";
    }
}
