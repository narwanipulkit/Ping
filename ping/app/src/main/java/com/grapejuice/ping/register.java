package com.grapejuice.ping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.widget.AppCompatButton;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pulkitnarwani on 30/10/15.
 */
public class register extends Activity implements View.OnClickListener {
    EditText u,e,p;
    String email,user,pass;
    AppCompatButton r;
    JSONObject reg=new JSONObject();
    String response;
    String link="http://fierce-shore-8534.herokuapp.com/api/register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login1);
        r=(AppCompatButton)findViewById(R.id.reg);
        u=(EditText)findViewById(R.id.un);
        e=(EditText)findViewById(R.id.email);
        p=(EditText)findViewById(R.id.pa);
        r.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        email=e.getText().toString();
        user=u.getText().toString();
        pass=p.getText().toString();

        Toast.makeText(getBaseContext(), validate().toString(), Toast.LENGTH_LONG).show();
        if(validate())
        {
            Thread t = new Thread() {
                public void run() {
                    try {

                        reg.put("user_id", user);
                        reg.put("password", pass);
                        //reg.put("email", email);
                        URL u = new URL(link);
                        HttpURLConnection con = (HttpURLConnection) u.openConnection();

                        con.setRequestMethod("POST");
                        con.setDoInput(true);
                        con.setDoOutput(true);
                       
                        con.connect();
                        DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                        dos.writeBytes("username="+reg.get("user_id")+"password="+reg.get("password"));
                        response=con.getResponseMessage();


                        Looper.prepare();

                        Toast resp= Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG);

                        resp.show();
                        Looper.loop();
                        dos.flush();


                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                }
            };
            t.start();
            try {
                t.join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            finally
            {
                Intent i=new Intent(this,login.class);
                startActivity(i);
            }

        }

    }
    public Boolean validate()
    {
        Boolean bu=false,bp=false,be=false;
        if(email!=null&& Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {be=true;

        }
        else
        {
            e.setError("Enter valid email address");
        }
        if(user!=null)
        {
            bu=true;

        }
        else
        {
            u.setError("Enter Username");
        }
        if(pass!=null)
        {
            bp=true;

        }
        else
        {
            p.setError("Enter Password");
        }
        if(bu&&bp&&be)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
