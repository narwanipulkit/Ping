package com.grapejuice.ping;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pulkitnarwani on 27/02/16.
 */
public class fetch1 extends Activity {

    private static final String link = "http://fierce-shore-8534.herokuapp.com/api/news/";
    static JSONObject ret = new JSONObject();
    Context c=getBaseContext();
    public JSONObject getJson(Context c) {
        try {
            login to=new login();
            URL u = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            String auth=new String();
            auth="JWT "+ to.token(c);
            Log.e("auth",auth);
            connection.setRequestProperty("Authorization",auth);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer();
            String tmp = "";
            while ((tmp = reader.readLine()) != null) {
                json.append(tmp).append("\n");

            }
            reader.close();
            JSONObject data = new JSONObject(json.toString());
            return data;
        } catch (MalformedURLException e) {

            e.printStackTrace();
            Toast t = Toast.makeText(c, "Check Your Internet Connection", Toast.LENGTH_SHORT);
            t.show();
            return ret;

        } catch (NetworkOnMainThreadException e) {


            e.printStackTrace();
            return ret;
        } catch (IOException e) {

            e.printStackTrace();
            Toast t = Toast.makeText(c, "Error", Toast.LENGTH_SHORT);
            t.show();
            return ret;
        } catch (JSONException e) {
            e.printStackTrace();
            Toast t = Toast.makeText(c, "Server Down", Toast.LENGTH_SHORT);
            t.show();
            return ret;
        }

    }


    public String getNews(JSONObject a,int objn,Context c)
    {
        String b;
        String ret="";
        try {
            JSONObject t=a.getJSONArray("items").getJSONObject(objn);
            b=t.getString("content");

        }
        catch (JSONException e)
        {
            e.printStackTrace();

            return ret;
        }
        return b;
    }
    public String getUser(JSONObject a,int objn,Context c)
    {
        String b;
        String ret="";
        try {
            JSONObject t=a.getJSONArray("items").getJSONObject(objn);
            b=t.getString("user_id");

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Toast t= Toast.makeText(c, "Error", Toast.LENGTH_SHORT);
            t.show();
            return ret;
        }
        return b;
    }

}
