package com.example.android.mymovie_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ahmed Khattab 2 on 9/16/2016.
 */
public class baseadapter_reviews extends BaseAdapter {
   LayoutInflater mInflater = null;
    Context mContext;
    ArrayList<JSONObject> mList;

    public baseadapter_reviews(Context mcontext, ArrayList<JSONObject> mlist) {
        this.mContext = mcontext;
        this.mList = mlist;
    }

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
        mInflater = (LayoutInflater) this.mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.single_row_review, viewGroup, false);
        TextView content = (TextView) view.findViewById(R.id.tv_review);
            content.setText(mList.get(i).getString("author").toString() + "     " + mList.get(i).getString("content").toString()  );


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
