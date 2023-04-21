package com.forgefrontier.forgefrontier.utils;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import com.forgefrontier.forgefrontier.tutorial.TaskStatus;
import org.checkerframework.checker.units.qual.A;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JSONWrapper {

    private static final JSONParser parser;

    static {
        parser = new JSONParser();
    }

    JSONObject object;

    public JSONWrapper(String json) {
        this.object = JSONWrapper.parse(json);
    }

    public JSONWrapper() {
        this.object = new JSONObject();
    }

    public JSONWrapper(JSONObject object) {
        this.object = object;
    }

    public static JSONObject parse(String json) {
        try {
            return (JSONObject) parser.parse(json);
        } catch(ParseException e) {
            ForgeFrontier.getInstance().getLogger().severe("Unable to parse json string: " + json);
            return null;
        }
    }

    public static JSONArray parseArray(String json) {
        try {
            return (JSONArray) parser.parse(json);
        } catch(ParseException e) {
            ForgeFrontier.getInstance().getLogger().severe("Unable to parse json string: " + json);
            return null;
        }
    }

    public static List<JSONWrapper> parseList(String json) {
        List<JSONWrapper> wrapperList = new ArrayList<>();
        List<JSONObject> objectList;
        try {
            objectList = (List<JSONObject>) parser.parse(json);
        } catch(ParseException e) {
            ForgeFrontier.getInstance().getLogger().severe("Unable to parse json string: " + json);
            return null;
        }
        for(JSONObject object: objectList) {
            wrapperList.add(new JSONWrapper(object));
        }
        return wrapperList;
    }

    public static JSONWrapper parseMap(Map<?, ? extends JSONSerializable> map) {
        JSONWrapper wrapper = new JSONWrapper();
        for(Object key: map.keySet()) {
            wrapper.object.put(key, map.get(key).toJSON().object);
        }
        return wrapper;
    }

    public String getString(String s) {
        if(!this.has(s))
            return null;
        return (String) this.object.get(s);
    }

    public Integer getInt(String s) {
        if(!this.has(s))
            return null;
        if(this.object.get(s) instanceof Long)
            return ((Long) this.object.get(s)).intValue();
        return (Integer) this.object.get(s);
    }

    public Boolean getBool(String s) {
        if(!this.has(s))
            return null;
        if(this.object.get(s) instanceof Boolean)
            return (Boolean) this.object.get(s);
        return (Boolean) this.object.get(s);
    }



    public boolean has(String s) {
        return this.object.containsKey(s);
    }

    public List<JSONWrapper> getList(String s) {
        if(!this.has(s))
            return null;
        List<Object> arr = (List<Object>) this.object.get(s);
        List<JSONWrapper> wrapperList = new ArrayList<>();
        for(Object object: arr) {
            wrapperList.add(new JSONWrapper((JSONObject) object));
        }
        return wrapperList;
    }

    public void setString(String key, String value) {
        this.object.put(key, value);
    }

    public void setInt(String key, int value) {
        this.object.put(key, value);
    }

    public String toJSONString() {
        return this.object.toJSONString();
    }

    public List<String> getStringKeys() {
        List<String> keyList = new ArrayList<>();
        for(Object key: this.object.keySet()) {
            keyList.add((String) key);
        }
        return keyList;
    }

    public JSONWrapper getJson(String key) {
        return new JSONWrapper((JSONObject) this.object.get(key));
    }

}
