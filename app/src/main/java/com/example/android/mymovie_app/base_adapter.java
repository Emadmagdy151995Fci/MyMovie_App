package com.example.android.mymovie_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by WiN  7 on 10/09/2016.
 */

public class base_adapter extends BaseAdapter {
    LayoutInflater layoutInflater= null;
    Context c;
    ArrayList<JSONObject>list;

    public base_adapter(Context c, ArrayList<JSONObject> list) {
        this.c = c;
        this.list = list;
    }

    @Override

    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {

        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        try {
            layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.main_poster, viewGroup, false);
            ImageView poster = (ImageView) view.findViewById(R.id.main_poster);
            String posters = "http://image.tmdb.org/t/p/w185/"+list.get(i).getString("poster_path").toString();
            Picasso.with(c).load(posters).into(poster);
        } catch (JSONException e) {
            e.printStackTrace();
        }

           /* Log.e("ff",db_poster);
            String title = list.get(i).getString("original_title").toString();
            String release_date = list.get(i).getString("release_date").toString();
            String vote=list.get(i).getString("vote_average").toString();
            String overview =list.get(i).getString("overview").toString();*/


        return view;
    }
}
