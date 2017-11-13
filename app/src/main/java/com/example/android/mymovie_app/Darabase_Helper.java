package com.example.android.mymovie_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by WiN  7 on 13/09/2016.
 */

public class Darabase_Helper extends SQLiteOpenHelper {
    public static final String create_table = "CREATE TABLE "+Contract.data.table_name +"("+Contract.data.col1_movie_id+" TEXT NOT NULL, "+Contract.data.col2_image+" TEXT  NOT NULL UNIQUE, "+Contract.data.col3_title+" TEXT NOT NULL ,"+Contract.data.col4_date+" TEXT NOT NULL ,"+Contract.data.col5_vote+" TEXT NOT NULL ,"+Contract.data.col6_overview+" TEXT NOT NULL"+")" ;
    public Darabase_Helper(Context context) {
        super(context,Contract.data.database_name,null,Contract.data.database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Contract.data.table_name);
        onCreate(sqLiteDatabase);

    }
    public boolean insert_data (String movie_id,String img,String title,String date,String vote , String overview)
    {



            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.data.col1_movie_id, movie_id);
            contentValues.put(Contract.data.col2_image, img);
            contentValues.put(Contract.data.col3_title, title);
            contentValues.put(Contract.data.col4_date, date);
            contentValues.put(Contract.data.col5_vote, vote);
            contentValues.put(Contract.data.col6_overview, overview);
            long res = db.insert(Contract.data.table_name, null, contentValues);




            if (res == -1) {
                return false;
            } else {
                return true;
            }

    }
    public Cursor view_posters(){
        SQLiteDatabase db = this.getReadableDatabase();
        String qury = "select * from " +Contract.data.table_name;
        Cursor cr = db.rawQuery(qury,null);
        return cr;
    }
    public Cursor view_data_byposter_path(String poster_path){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cr =db.query(Contract.data.table_name,new String[]{Contract.data.col1_movie_id,Contract.data.col2_image,Contract.data.col3_title,Contract.data.col4_date,Contract.data.col5_vote,Contract.data.col6_overview},Contract.data.col2_image +" =?",new String[]{poster_path},null,null,null);
        return cr;
    }
    public void delete_poster(String poster){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Contract.data.table_name,Contract.data.col2_image+" =?",new String[]{poster});
    }
}
