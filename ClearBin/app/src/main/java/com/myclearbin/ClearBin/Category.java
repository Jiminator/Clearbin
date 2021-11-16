package com.myclearbin.ClearBin;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;

public class Category {
    public ArrayList<Integer> category = new ArrayList<>();
    public String name;
    public double[] stats;
    public String description;
    public String reduce;
    public String reuse;

    public int id;
    public long searches;

    public Category(String name, int id, String description, double[] stats, String reduce, String reuse, long searches) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.stats = stats;
        this.reduce = reduce;
        this.reuse = reuse;
        this.searches = searches;
    }

    public void incrementSearch(){
        this.searches++;
    }

    @Override
    public String toString() {
        return "\nName: "+name+"\nId: "+id+"\nDescription: "+description+"\nStats: "+ Arrays.toString(stats)+"\nReduce: "+reduce+"\nReuse: "+reuse+"\nSearches: "+searches;
    }

    public JSONObject toJSONObject(){
        try {
            return new JSONObject().put("Name", name).put("Id", id).put("Description", description).put("Stats", Arrays.toString(stats)).put("Reduce", reduce).put("Reuse", reuse).put("Searches", searches);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
