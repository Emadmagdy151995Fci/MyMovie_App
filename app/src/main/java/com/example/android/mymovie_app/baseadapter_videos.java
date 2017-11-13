package com.example.android.mymovie_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ahmed Khattab 2 on 9/16/2016.
 */
public class baseadapter_videos extends BaseAdapter {
    Context c;
    ArrayList<JSONObject> mList;

    public baseadapter_videos(Context c, ArrayList<JSONObject> list) {
        this.c = c;
        mList = list;
    }

    LayoutInflater mInflater = null;

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        try {
            mInflater = (LayoutInflater) this.c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.single_row_video_trailer, viewGroup, false);
           //final String key = mList.get(i).getString("key").toString();

            TextView name_trailer = (TextView) view.findViewById(R.id.trailer_name);
           // ListView listView = (ListView) view.findViewById(R.id.lv_video);


            Log.e("aa",mList.get(i).getString("name").toString());
            name_trailer.setText(mList.get(i).getString("name").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
