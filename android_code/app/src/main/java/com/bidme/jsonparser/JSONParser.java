package com.bidme.jsonparser;

import android.content.Context;

import com.bidme.preferences.SharedPrefrence;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

/**
 * Created by VARUN on 01/01/19.
 */

public class JSONParser {
    String jsonObjResponse;
    public String SUCCESS = "";
    public String MESSAGE = "";
    public boolean RESULT = false;
    public Context context;
    public static String PIC_KEY = "profilepic";
    public JSONObject jObj;


    public static String TAG_SUCCESS = "status";
    public static String TAG_MESSAGE = "message";
    private SharedPrefrence prefrence;

    public JSONParser(Context context, JSONObject response) {
        try {
            this.context = context;
            jObj = response;
            SUCCESS = getJsonString(jObj, TAG_SUCCESS);
            MESSAGE = html2text(getJsonString(jObj, TAG_MESSAGE));
            prefrence = SharedPrefrence.getInstance(context);
            if (SUCCESS.equals("0")) {
                RESULT = false;

            }/* else if (SUCCESS.equals("3")) {
                prefrence.clearAllPreferences();
                Intent intent = new Intent(context, SignIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }*/ else {
                RESULT = true;

            }

        } catch (Exception e) {
            jObj = null;
            e.printStackTrace();
        }
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    public static boolean getBoolean(String val) {
        if (val.equals("true"))
            return true;
        else
            return false;
    }

    public static JSONObject getJsonObject(JSONObject obj, String parameter) {
        try {
            return obj.getJSONObject(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }

    }

    public static String getJsonString(JSONObject obj, String parameter) {
        try {
            String val = obj.getString(parameter);
            if (val != null) {
                if (val.equalsIgnoreCase("null") || val.equalsIgnoreCase(""))
                    return "";
                else
                    return val;
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static JSONArray getJsonArray(JSONObject obj, String parameter) {
        try {
            return obj.getJSONArray(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }

    }


}