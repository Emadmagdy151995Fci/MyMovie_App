package com.example.android.mymovie_app;

import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements main_frgment.movie_interface {
    Darabase_Helper helper;
    String img_path,m_title,m_date,m_vote,m_overview;
    boolean mtwo_pane=false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new main_frgment()).commit();
        }
        helper = new Darabase_Helper(MainActivity.this);


     if (findViewById(R.id.frg2_container) == null){
         mtwo_pane = false;
     }
        else {
         mtwo_pane = true;
     }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void data(String movie_id, String movie_poster_path, String movie_title, String movie_date, String movie_vote, String movie_overview) {
        if (mtwo_pane == false){
            Intent i = new Intent(this,activity_datails.class);
            i.putExtra("id",movie_id);
            i.putExtra("poster_path",movie_poster_path);
            i.putExtra("title",movie_title);
            i.putExtra("date",movie_date);
            i.putExtra("vote",movie_vote);
            i.putExtra("overview",movie_overview);
            startActivity(i);
        }
        else {
            details_fragment df = new details_fragment();
            Bundle bundle = new Bundle();
            bundle.putString("id",movie_id);
            bundle.putString("poster_path",movie_poster_path);
            bundle.putString("title",movie_title);
            bundle.putString("date",movie_date);
            bundle.putString("vote",movie_vote);
            bundle.putString("overview",movie_overview);
            df.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frg2_container,df).commit();
        }
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


}
