package com.grapejuice.ping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pulkitnarwani on 30/10/15.
 */
public class post extends Activity  {

    EditText b;
    //Context c=getBaseContext();
    FloatingActionButton submit;
    AppCompatButton cancel;
    String username,news;
    JSONObject put_news;
    String response;
    String pass,user;
    String link="http://fierce-shore-8534.herokuapp.com/api/news";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        //a=(EditText)findViewById(R.id.newsubmit);

        submit=(FloatingActionButton)findViewById(R.id.s);




        //readdet();



    }

    public void postAndSubmit(Context c,final String n)
    {
        SharedPreferences s=c.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        final String token=s.getString("token",null);
        final login to =new login();

        put_news=new JSONObject();
        final String auth="JWT "+ token;

        //ac.setContentView(R.layout.app_bar_main);
       //final EditText a=(EditText)ac.findViewById(R.id.newsubmit);
        //Log.e("asd", a.getText().toString());
        Thread t = new Thread() {

            public void run() {

                news = n;
                try {




                    put_news.put("content", news);

                    URL u = new URL(link);
                    HttpURLConnection con = (HttpURLConnection) u.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Authorization",auth);
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setRequestProperty("Content-Type", "application/json");
                    con.connect();
                    DataOutputStream osw = new DataOutputStream(con.getOutputStream());
                    osw.writeBytes(put_news.toString());
                    osw.flush();
                    response = con.getResponseMessage();
                    Log.e("Response MEssage",response);
                    osw.close();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();


    }


/*
    void readdet()
    {
        try {
            FileInputStream fis=openFileInput("flag.txt");
            InputStreamReader in=new InputStreamReader(fis);
            BufferedReader br=new BufferedReader(in);
            StringBuilder p = new StringBuilder();
            String t;
            if(in!=null) {
                while ((t = br.readLine()) != null)

                {
                    p.append(t);
                }
            }

            try {
                JSONObject out=new JSONObject(p.toString());
                pass=out.getString("password");
                user=out.getString("user_id");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    }


*/
}
