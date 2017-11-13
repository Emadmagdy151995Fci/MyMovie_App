package com.example.android.mymovie_app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by WiN  7 on 10/09/2016.
 */

public class details_fragment extends Fragment {
    ListView lv_video, lv_review;
    Button video, review;
    ImageView mposter;
    TextView mtitle, mdate, mvote, moverview;
    ToggleButton fav;
    Context context;
    String poster_path, title, date, vote, overview,postrs;
    String movie_id ="";
    Darabase_Helper helper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_fragment, container, false);
        context = view.getContext();
        helper = new Darabase_Helper(context);
        mposter = (ImageView) view.findViewById(R.id.poster);
        mtitle = (TextView) view.findViewById(R.id.title);
        mdate = (TextView) view.findViewById(R.id.year);
        mvote = (TextView) view.findViewById(R.id.vote);
        moverview = (TextView) view.findViewById(R.id.description);
        lv_review = (ListView) view.findViewById(R.id.lv_review);
        lv_video = (ListView) view.findViewById(R.id.lv_video);
        fav = (ToggleButton) view.findViewById(R.id.fav);
        video = (Button) view.findViewById(R.id.btn_videos);
        review = (Button) view.findViewById(R.id.btn_reviews);
        Bundle b = getArguments();
        if (b == null){
            Intent i = getActivity().getIntent();
            movie_id = i.getExtras().getString("id").toString();
            Log.e("2222222",movie_id);
            poster_path = i.getExtras().getString("poster_path");
             postrs ="http://image.tmdb.org/t/p/w185/"+poster_path;
            Log.e("paaaaaaaaaaa",poster_path);
            title = i.getExtras().getString("title");
            date = i.getExtras().getString("date").substring(0, 4);
            vote = i.getExtras().getString("vote");
            overview = i.getExtras().getString("overview");
        }else {
            movie_id = b.getString("id");
            poster_path = b.getString("poster_path");
            postrs ="http://image.tmdb.org/t/p/w185/"+poster_path;
            Log.e("thaaaaaaaaaaa",poster_path);
            title=b.getString("title");
            date=b.getString("date").substring(0,4);
            vote=b.getString("vote");
            overview=b.getString("overview");


        }

        Picasso.with(context).load(postrs).into(mposter);
        mtitle.setText(title);
        mdate.setText(date);
        mvote.setText(vote);
        moverview.setText(overview);



/////////
        helper = new Darabase_Helper(context);
        Cursor c = helper.view_data_byposter_path(poster_path);
        if (c.moveToFirst()==true){
            fav.setChecked(true);
        }
        else {
            fav.setChecked(false);
        }
        insert_data();

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new asyn_video().execute("http://api.themoviedb.org/3/movie/" + movie_id + "/videos?api_key=557e89e61b830a3a80dc356aac6596dc");
                lv_video.setVisibility(View.VISIBLE);
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new asyn_review().execute("http://api.themoviedb.org/3/movie/" + movie_id + "/reviews?api_key=557e89e61b830a3a80dc356aac6596dc");
                lv_review.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    private void insert_data() {

     fav.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             if(fav.isChecked() == true){
                 boolean result = helper.insert_data(movie_id,poster_path, title, date, vote, overview);

                 try {


                     if (result == true) {
                         Toast.makeText(context, "Data is inserted Succssfully", Toast.LENGTH_SHORT).show();
                     } else if (result == false) {

                         Toast.makeText(context, "choosed favourite before", Toast.LENGTH_SHORT).show();
                     }
                 } catch (Exception ex) {
                     Toast.makeText(context, "choosed favourite before", Toast.LENGTH_SHORT).show();
                 }

             }
             else {
                 helper.delete_poster(poster_path);
                 Toast.makeText(context, "deleted from favourite ", Toast.LENGTH_SHORT).show();

             }
         }
     });


    }

    public class asyn_review extends AsyncTask<String, Void, String> {
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        String data = "";
        String line = "";

        @Override
        protected String doInBackground(String... strings) {
            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                while ((line = bufferedReader.readLine()) != null) {
                    data += line;
                }
                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    httpURLConnection.disconnect();
                    inputStreamReader.close();
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                final ArrayList<JSONObject> trailer_review = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); ++i) {
                    trailer_review.add(jsonArray.getJSONObject(i));
                }
                lv_review.setAdapter(new baseadapter_reviews(context, trailer_review));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public class asyn_video extends AsyncTask<String, Void, String> {
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        String data = "";
        String line = "";

        @Override
        protected String doInBackground(String... strings) {
            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                while ((line = bufferedReader.readLine()) != null) {
                    data += line;
                }
                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    httpURLConnection.disconnect();
                    inputStreamReader.close();
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                final ArrayList<JSONObject> trailer_video = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); ++i) {
                    Log.e("ffff", "" + trailer_video.add(jsonArray.getJSONObject(i)));
                }
                lv_video.setAdapter(new baseadapter_videos(context, trailer_video));
                lv_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Context c = context;
                        try {
                            String key = trailer_video.get(i).getString("key");
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + key));
                            c.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
