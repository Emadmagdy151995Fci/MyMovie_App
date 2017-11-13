package com.example.android.mymovie_app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
import java.util.Objects;

/**
 * Created by WiN  7 on 10/09/2016.
 */

public class main_frgment extends Fragment {

    GridView gridView;
    Context context;
    Darabase_Helper helper;
    String movie_id;
    String img_path,mposter_id,mposter_title,mposter_date,mposter_vote,mposter_overview;
    base_adapter adapter;
    movie_interface movieinterface;
    static String movie_state = null;
    public main_frgment(){
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment, container, false);
        gridView = (GridView) view.findViewById(R.id.grid_view);
        helper = new Darabase_Helper(context);
        final base_adapter adapter = new base_adapter(context, new ArrayList<JSONObject>());
        gridView.setAdapter(adapter);
        context = view.getContext();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object object = adapterView.getAdapter().getItem(i);
                JSONObject jsonObject = (JSONObject) object;
                try {
                   String poster_path= jsonObject.getString("poster_path");
                    Log.e("poster_path",poster_path);
                    String poster_id = jsonObject.getString("id");
                    String title=jsonObject.getString("original_title");
                    String date =jsonObject.getString("release_date");
                    String vote = jsonObject.getString("vote_average");
                    String overview =jsonObject.getString("overview");
///////
                    movieinterface.data(poster_id,poster_path,title,date,vote,overview);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if (id == R.id.favourit) {
            movie_state = "favourite";
            final Darabase_Helper helper = new Darabase_Helper(context);
            final Cursor cursor = helper.view_posters();
            ArrayList<JSONObject> list=new ArrayList<>();
            JSONObject jsonObject ;
            while (cursor.moveToNext()) {
                try {
                jsonObject = new JSONObject();
                img_path = cursor.getString(cursor.getColumnIndex(Contract.data.col2_image));
                mposter_id=cursor.getString(cursor.getColumnIndex(Contract.data.col1_movie_id));
                mposter_title=cursor.getString(cursor.getColumnIndex(Contract.data.col3_title));
                mposter_date=cursor.getString(cursor.getColumnIndex(Contract.data.col4_date));
                mposter_vote=cursor.getString(cursor.getColumnIndex(Contract.data.col5_vote));
                mposter_overview=cursor.getString(cursor.getColumnIndex(Contract.data.col6_overview));

                    jsonObject.put("poster_path",img_path);
                    jsonObject.put("id",mposter_id);
                    jsonObject.put("original_title",mposter_title);
                    jsonObject.put("release_date",mposter_date);
                    jsonObject.put("vote_average",mposter_vote);
                    jsonObject.put("overview",mposter_overview);
                    list.add(jsonObject);
                    Log.e("1111111111111111111111", String.valueOf(list));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
             base_adapter adapter =new base_adapter(context,list);
             gridView.setAdapter(adapter);


        } else if (R.id.top_rated == id) {
            movie_state = "top_rated";
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                new Asyn_task().execute("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=8b68723135daac2465e29878aecbf0f5");
            }
            else{
                Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.popular) {
            movie_state = "popular";
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                new Asyn_task().execute("http://api.themoviedb.org/3/movie/popular?api_key=8b68723135daac2465e29878aecbf0f5");

            }
            else{
                Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_LONG).show();
            }



        }

        return super.onOptionsItemSelected(item);
    }
///////////
    @Override
    public void onStart() {
        super.onStart();
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
if (movie_state == null){
    new Asyn_task().execute("http://api.themoviedb.org/3/movie/popular?api_key=8b68723135daac2465e29878aecbf0f5");
}
            else if(movie_state.equals("popular")){
    new Asyn_task().execute("http://api.themoviedb.org/3/movie/popular?api_key=8b68723135daac2465e29878aecbf0f5");
}
            else if (movie_state.equals("top_rated")){
    new Asyn_task().execute("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=8b68723135daac2465e29878aecbf0f5");

}
            else if (movie_state.equals("favourite")){
    movie_state = "favourite";
    final Darabase_Helper helper = new Darabase_Helper(context);
    final Cursor cursor = helper.view_posters();
    ArrayList<JSONObject> list=new ArrayList<>();
    JSONObject jsonObject ;
    while (cursor.moveToNext()) {
        try {
            jsonObject = new JSONObject();
            img_path = cursor.getString(cursor.getColumnIndex(Contract.data.col2_image));
            mposter_id=cursor.getString(cursor.getColumnIndex(Contract.data.col1_movie_id));
            mposter_title=cursor.getString(cursor.getColumnIndex(Contract.data.col3_title));
            mposter_date=cursor.getString(cursor.getColumnIndex(Contract.data.col4_date));
            mposter_vote=cursor.getString(cursor.getColumnIndex(Contract.data.col5_vote));
            mposter_overview=cursor.getString(cursor.getColumnIndex(Contract.data.col6_overview));

            jsonObject.put("poster_path",img_path);
            jsonObject.put("id",mposter_id);
            jsonObject.put("original_title",mposter_title);
            jsonObject.put("release_date",mposter_date);
            jsonObject.put("vote_average",mposter_vote);
            jsonObject.put("overview",mposter_overview);
            list.add(jsonObject);
            Log.e("1111111111111111111111", String.valueOf(list));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    base_adapter adapter =new base_adapter(context,list);
    gridView.setAdapter(adapter);

}
        }
        else{
            Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public class Asyn_task extends AsyncTask<String, Void, String> {
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
                JSONObject json_data ;
                ArrayList<JSONObject> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); ++i) {
                    json_data = jsonArray.getJSONObject(i);
                    list.add(json_data);
                }



                adapter = new base_adapter(context,list);
                gridView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
    public interface movie_interface{
        void data(String movie_id,String movie_poster_path,String movie_title,String movie_date,String movie_vote,String movie_overview);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof movie_interface) {
            movieinterface = (movie_interface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement movie_listner");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        movieinterface = null;
    }
}
