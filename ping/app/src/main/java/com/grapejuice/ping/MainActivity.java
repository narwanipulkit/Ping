package com.grapejuice.ping;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CharSequence mTitle;
    fetch1 dataFetch=new fetch1();
    SwipeRefreshLayout srl;
    ListView navMenu;
    TextView a,b;
    TextView navText;
    ListView ls;
    RecyclerView rv;
    Handler hand=new Handler();
    FloatingActionButton fab1;
    Thread t,n;
    String uname;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navText=(TextView)findViewById(R.id.navuser);
        readData();
        //navText.setText(uname);

        srl=(SwipeRefreshLayout)findViewById(R.id.activity_main_swipe_refresh_layout);
        //To resolve Network error
        if(Build.VERSION.SDK_INT>9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        srl.setColorSchemeColors(Color.BLACK, Color.BLUE, Color.RED);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Toast ref = Toast.makeText(getBaseContext(), "Refreshing..", Toast.LENGTH_SHORT);
                ref.show();
                n= new Thread() {

                public void run() {
                    hand.post(refreshing);
                }
                };
                n.start();
                //srl.postDelayed(refreshing,2000);
            }
        });
        genList();









        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.s);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
                /*//OLD VERSION INTENT --REMOVED IN CURRENT VERSION
                 Intent i;
                i = new Intent(getBaseContext(),post.class);
                startActivity(i);*/
                EditText txt=(EditText)findViewById(R.id.newsubmit);
                Log.e("asda", txt.getText().toString());
                post a=new post();

                a.postAndSubmit(getBaseContext(),txt.getText().toString());
                genList();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private final Runnable refreshing=new Runnable() {
        @Override
        public void run() {

            genList();



        }



    };


    //--------------------------Function for getting JSON and Displaying-----------------------------//
    public synchronized void genList()
    {

        rv=(RecyclerView)findViewById(R.id.rv);
        a=(TextView)findViewById(R.id.headline1);
        b=(TextView)findViewById(R.id.by1);
        //a.setText("klk");
        LinearLayoutManager a=new LinearLayoutManager(this);
        rv.setLayoutManager(a);
        List<data> aa = new ArrayList<data>();

        RVAdapter adapter = new RVAdapter(aa,getBaseContext());
        rv.setAdapter(adapter);


        t=new Thread() {
            public void run() {
                hand.post(new Runnable() {

                    public void run() {
                        final JSONObject r = dataFetch.getJson(getBaseContext());
                        int i = 0;
                        data d1 = new data();
                        List<data> aa = new ArrayList<data>();
                        while(dataFetch.getNews(r,i,getBaseContext())!="") {

                            data d = new data(dataFetch.getNews(r, i, getBaseContext()), dataFetch.getUser(r, i, getBaseContext()));
                            aa.add(d);

                            i++;
                        }
                        //List<data> aa=d1.getList();
                        java.util.Collections.reverse(aa);
                        RVAdapter adapter = new RVAdapter(aa,getBaseContext());
                        rv.setAdapter(adapter);
                    }

                });

            }
        };
        t.start();
        try {
            t.join();
                if(!t.isAlive())
                {

                    srl.setRefreshing(false);
                }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }



    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            Toast ref = Toast.makeText(getBaseContext(), "Refreshing..", Toast.LENGTH_SHORT);
            ref.show();
            //srl.postDelayed(refreshing,2000);
            hand.post(refreshing);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i=new Intent(getBaseContext(),MainActivity.class);
            startActivity(i);
            this.finish();

        } else if (id == R.id.nav_group) {


        } else if (id == R.id.nav_post) {
            Intent i=new Intent(getBaseContext(),post.class);
            startActivity(i);

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void readData()
    {
        try {
            FileInputStream fis=openFileInput("flag.txt");
            InputStreamReader in=new InputStreamReader(fis);
            BufferedReader br=new BufferedReader(in);
            String t;
            StringBuilder sb=new StringBuilder();
            if(in!=null)
            {
                while((t=br.readLine())!=null);
                {
                    sb.append(t);
                }
                uname=sb.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
