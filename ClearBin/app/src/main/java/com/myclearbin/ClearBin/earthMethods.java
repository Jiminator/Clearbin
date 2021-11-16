package com.myclearbin.ClearBin;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class earthMethods {
    private static final String API_KEY = "1662cbe9480aeef4";

    private static final String keyString = "?api_key="+API_KEY;

    public static String baseUrl = "http://api.earth911.com/earth911.";
    public static File itemFile = new File("/Users/5apphire/Desktop/Files/MyClearBin/src/items.txt");
    public static File categoryFile = new File("/Users/5apphire/Desktop/Files/MyClearBin/src/categories.txt");


    public static void main(String[] args) {

//        for (Item item: earthItemList(getMaterials())) {
//            System.out.println(item+"\n");
//        }
//        JSONArraytoFile(toItemJSONArray(toItemList(getMaterials())),itemFile);
//        JSONArray result = FileToJSONArray(itemFile);
//
//        JSONArraytoFile(toCategoryJSONArray(toCategoryList(getFamilies())),categoryFile);
//        JSONArray result1 = FileToJSONArray(categoryFile);
//        System.out.println(result.toString());
//        System.out.println(result1);

//        System.out.println(searchMaterials(7,"plastic%20bottle"));
//        System.out.println(searchListings(true,false,true,37.4177,-122.20,2,20,100));
//        System.out.println(getLocationTypes().toString());
//        System.out.println(getProgramTypes().toString());
//
//        System.out.println(getLocationDetails("Q1RQNVJYXF1GVQ"));
//        System.out.println(getLocationRestrictions("Q1RQNVJYXF1GVQ"));
//
//        System.out.println(getProgramDetails("Q1RQNVBbU1dAUQ"));
//        System.out.println(getProgramRestrictions("Q1RQNVBbU1dAUQ"));
//        System.out.println("hi");

    }


    public static JSONArray getMaterials(){
        String parsedJson = jsonParser(baseUrl+"getMaterials"+keyString);

        JSONObject materials = new JSONObject();
        try {
            if (parsedJson != null) {
                materials = new JSONObject(parsedJson);
            }

            return materials.getJSONArray("result");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getFamilies(){
        String parsedJson = jsonParser(baseUrl+"getFamilies"+keyString);

        JSONObject materials = new JSONObject();
        
       try {
            if (parsedJson != null) {
                materials = new JSONObject(parsedJson);
            }

            return materials.getJSONArray("result");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getLocationDetails(String id){
        String parsedJson = jsonParser(baseUrl+"getLocationDetails"+keyString+"&location_id="+id);

        JSONObject materials = new JSONObject();
        
        try {
            if (parsedJson != null) {
                materials = new JSONObject(parsedJson);
            }

            return materials.getJSONObject("result");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getLocationRestrictions(String id){
        String parsedJson = jsonParser(baseUrl+"getLocationRestrictions"+keyString+"&location_id="+id);

        JSONObject materials = new JSONObject();
        
        try {
            if (parsedJson != null) {
                materials = new JSONObject(parsedJson);
            }

            return materials.getJSONObject("result");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getLocationTypes(){
        String parsedJson = jsonParser(baseUrl+"getLocationTypes"+keyString);

        JSONObject materials = new JSONObject();
        
       try {
            if (parsedJson != null) {
                materials = new JSONObject(parsedJson);
            }

            return materials.getJSONArray("result");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getProgramDetails(String id){
        String parsedJson = jsonParser(baseUrl+"getProgramDetails"+keyString+"&program_id="+id);
       
        JSONObject materials = new JSONObject();

        try {
            if (parsedJson != null) {
                materials = new JSONObject(parsedJson);
            }

            return materials.getJSONObject("result");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getProgramRestrictions(String id){
        String parsedJson = jsonParser(baseUrl+"getProgramRestrictions"+keyString+"&program_id="+id);

        JSONObject materials = new JSONObject();

        try {
            if (parsedJson != null) {
                materials = new JSONObject(parsedJson);
            }

            return materials.getJSONObject("result");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getProgramTypes(){
        String parsedJson = jsonParser(baseUrl+"getProgramTypes"+keyString);

        JSONObject materials = new JSONObject();
        
       try {
            if (parsedJson != null) {
                materials = new JSONObject(parsedJson);
            }

            return materials.getJSONArray("result");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray searchMaterials(int max_results, String query){
        String parsedJson = jsonParser(baseUrl+"searchMaterials"+keyString+"&max_results="+max_results+"&query="+query);

        JSONObject materials = new JSONObject();
        
       try {
            if (parsedJson != null) {
                materials = new JSONObject(parsedJson);
            }

            return materials.getJSONArray("result");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray searchListings(boolean business, boolean residential, boolean hideEvent, double latitude, double longitude, int material_id, int max_distance, int max_results){

        String parsedJson = jsonParser(baseUrl+"searchListings"+keyString+"&business_only="+business+"&residential_only="+residential+"&hide_event_only="+hideEvent+"&latitude="+latitude+"&longitude="+longitude+"&material_id="+material_id+"&max_distance="+max_distance+"&max_results="+max_results);

        JSONObject materials = new JSONObject();
        
       try {
            if (parsedJson != null) {
                materials = new JSONObject(parsedJson);
            }

            return materials.getJSONArray("result");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

//    -------------------------------------------------------------------

    public static void JSONArraytoFile(JSONArray List, File file){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            JSONObject items = new JSONObject();

            items.put("result",List);

            writer.write(items.toString());
            writer.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static JSONArray FileToJSONArray(File file){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String out = reader.readLine();
            return  new JSONObject(out).getJSONArray("result");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<Category> toCategoryList(JSONArray families){
        ArrayList<Category> categoryList = new ArrayList<>();

        try {
            for (int i = 0; i < families.length(); i++) {
                JSONObject item = families.getJSONObject(i);
                if (item.get("description").toString().equals("Reuse")) {
                    break;
                } else if (!item.get("description").toString().equals("All Materials")) {
                    categoryList.add(new Category(
                            item.get("description").toString(),
                            Integer.parseInt(item.get("family_id").toString()),
                            null,
                            null,
                            null,
                            null,
                            0
                    ));
//               String name, int id, String description, double[] stats, String reduce, String reuse, long searches) {
                } else {

                }

            }
            return categoryList;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<Item> toItemList(JSONArray materials) throws Exception {
        ArrayList<Item> itemList = new ArrayList<>();

        for (int i = 0; i < materials.length(); i++) {
            JSONObject item = materials.getJSONObject(i);
            itemList.add(new Item(item.get("description").toString(), Integer.parseInt(item.get("material_id").toString()), -1, item.get("long_description").toString(), true, 0, item.get("image").toString(), ""));

        }

        return itemList;
    }


    public static JSONArray toItemJSONArray(ArrayList<Item> List){
        JSONArray result = new JSONArray();
        try {
            for (Item item : List) {
                result.put(item.toJSONObject());
            }

            return result;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray toCategoryJSONArray(ArrayList<Category> List){
        JSONArray result = new JSONArray();

        for (Category category: List) {
            result.put(category.toJSONObject());
        }

        return result;
    }

    private static String jsonParser(String urlString){
        String result = "";
        URL url;
        HttpURLConnection urlConnection;

        try {
            url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();
            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
