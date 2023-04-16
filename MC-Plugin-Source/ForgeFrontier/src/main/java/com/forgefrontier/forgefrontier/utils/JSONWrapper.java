package com.forgefrontier.forgefrontier.utils;

import com.forgefrontier.forgefrontier.ForgeFrontier;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class JSONWrapper {

    private static final JSONParser parser;

    static {
        parser = new JSONParser();
    }

    JSONObject object;

    public JSONWrapper(String json) {
        this.object = JSONWrapper.parse(json);
    }

    public static JSONObject parse(String json) {
        try {
            return (JSONObject) parser.parse(json);
        } catch(ParseException e) {
            ForgeFrontier.getInstance().getLogger().severe("Unable to parse json string: " + json);
            return null;
        }
    }

    public JSONWrapper(JSONObject object) {
        this.object = object;
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

    public String getString(String s) {
        if(!this.has(s))
            return null;
        return (String) this.object.get(s);
    }

    public Integer getInt(String s) {
        if(!this.has(s))
            return null;
        return (Integer) this.object.get(s);
    }

    public boolean has(String s) {
        return this.object.containsKey(s);
    }

    public List<JSONWrapper> getList(String s) {
        if(!this.has(s))
            return null;
        List<JSONWrapper> wrapperList = new ArrayList<>();
        for(Object object: (List<Object>) this.object.get(s)) {
            wrapperList.add(new JSONWrapper((JSONObject) object));
        }
        return wrapperList;
    }

    public void setString(String key, String value) {
        this.object.put(key, value);
    }

    public String toJSONString() {
        return this.object.toJSONString();
    }
}
