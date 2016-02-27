package com.grapejuice.ping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pulkitnarwani on 30/10/15.
 */
public class login extends Activity implements View.OnClickListener {
    AppCompatButton login_button;
    AppCompatTextView newuser;
    EditText n,e,p;
    String na,em,pa;
    Boolean valide,validp;
    JSONObject response;
    String flag="false";
    FileInputStream iStream;
    Thread t;
    String link="http://fierce-shore-8534.herokuapp.com/api/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            flag=readFlag().get("flag").toString();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        if(flag.equals("true"))
        {
            Toast.makeText(this, "Please Wait", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            this.finish();
        }
        else {
            setContentView(R.layout.login);
            //Toast.makeText(getBaseContext(),flag,Toast.LENGTH_SHORT).show();
            newuser=(AppCompatTextView)findViewById(R.id.newu);
            newuser.setOnClickListener(this);
            login_button = (AppCompatButton) findViewById(R.id.login);
            login_button.setOnClickListener(this);
            e = (EditText) findViewById(R.id.uname);
            p = (EditText) findViewById(R.id.pass);

        }
    }

    @Override
    public void onClick(View view) {


        if(view.getId()==R.id.login) {


            em = e.getText().toString();
            pa = p.getText().toString();


            t = new Thread() {
                public void run() {
                    try {
                        JSONObject up = new JSONObject();
                        up.put("user_id", em);
                        up.put("password", pa);
                        URL u = new URL(link);
                        HttpURLConnection con = (HttpURLConnection) u.openConnection();
                        con.setRequestMethod("POST");


                        //con.setDoInput(true);
                        con.setDoOutput(true);

                        con.connect();


                        con.getOutputStream().write(("username=" + em + "&" + "password=" + pa).getBytes());
                       // response = con.getResponseMessage();
                       // Log.e("respon",response.toString());
                       BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));

                        StringBuffer resp = new StringBuffer();
                        String temp;

                       while((temp=br.readLine())!=null)
                        {
                            resp.append(temp);
                        }

                        response=new JSONObject(resp.toString());


                        Log.e("resp",resp.toString());



                        Looper.prepare();

//                        Toast resp = Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG);

//                        resp.show();
                        //                      Looper.loop();


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
            } finally {


                validate();


                try {
                    if (valide && validp && response.get("message").toString().equals("login Successful")) {
                        flag = "true";
                        try {
                            writeToFile(response.get("token").toString());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        Toast.makeText(this, "Please Wait", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                        this.finish();
                    } else {
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        }
        else if(view.getId()==R.id.newu)
        {
            Intent reg=new Intent(this,register.class);
            startActivity(reg);
        }
    }


    public JSONObject readFlag()
    {
        JSONObject f=new JSONObject();
        try {

            FileInputStream iStream=getApplication().openFileInput("flag.txt");
            InputStreamReader isr=new InputStreamReader(iStream);
            BufferedReader br=new BufferedReader(isr);
            StringBuilder sb=new StringBuilder();
            String out;
            if(isr!=null)
            {
                String t;
                while((t=br.readLine())!=null)
                {
                    sb.append(t);

                }
                iStream.close();
                out=sb.toString();

                try {
                    f=new JSONObject(out);

                    String fout=f.getString("flag");
                    Toast.makeText(getBaseContext(),fout,Toast.LENGTH_LONG).show();
                    return f;
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                return f;
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return new JSONObject();
        } catch (IOException e1) {
            e1.printStackTrace();
            return new JSONObject();
        }
        return new JSONObject();
    }

    public String token(Context c)
    {
        SharedPreferences s=c.getSharedPreferences("preferences",Context.MODE_PRIVATE);
        String asd=s.getString("token",null);
        return asd;
    }


    public void writeToFile(String token)
    {
        SharedPreferences s=getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=s.edit();
        editor.putString("token",token);
        editor.commit();
        try {
            JSONObject file=new JSONObject();
            try {
                file.put("user_id",em);
                file.put("password",pa);
                file.put("flag",flag);
                file.put("token", token);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            File f=new File("flag.txt");
            OutputStreamWriter osr=new OutputStreamWriter(getApplication().openFileOutput("flag.txt", Context.MODE_PRIVATE));
            osr.write(file.toString());
            osr.close();
            OutputStreamWriter pass=new OutputStreamWriter(openFileOutput("pass.txt",Context.MODE_PRIVATE));
            pass.write(pa);
            pass.close();


        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void validate()
    {
        if(em.isEmpty())
        {
            e.setError("Enter Valid Username");
            valide=false;

        }
        else
        {
            valide=true;
        }
        if(pa.isEmpty())
        {
            p.setError("Please Enter Password");
            validp=false;
        }
        else
        {
            validp=true;
        }
    }


}
