package com.example.android.mymovie_app;

import android.provider.BaseColumns;

/**
 * Created by WiN  7 on 13/09/2016.
 */

public class Contract implements BaseColumns {
    public static final class data{
        public static final String database_name ="movie_database";
        public static final int database_version = 1;
        public static final String table_name = "movie_details";
        public static final String col1_movie_id ="Movie_ID";
        public static final String col2_image ="Poster";
        public static final String col3_title = "Title";
        public static final String col4_date = "Release_Data";
        public static final String col5_vote = "Vote";
        public static final String col6_overview="OverView";
    }
}
