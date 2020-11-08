package com.example.movie.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;

public class CommentDatabase {


    private static final String TAG = "AppHelper";

    private static SQLiteDatabase database;

    private static String createTableCommentSql = "create table if not exists comment " +
            "(" +
            "    id integer PRIMARY KEY, " +
            "    writer text, " +
            "    movieId integer, " +
            "    writer_image text, " +
            "    time text, " +
            "    timestamp integer, " +
            "    rating float, " +
            "    contents String, " +
            "    recommend integer, " +
            "    totalCount integer " +
            ")";

    public static void openDatabase(Context context, String databaseName) {
        try {
            database = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
            if (database != null) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTable(String tableName) {

        if (database != null) {
            if (tableName.equals("comment")) {
                database.execSQL(createTableCommentSql);
            }
        } else {
        }
    }

    public static void insertCommentData(int id,
                                         String writer,
                                         int movieId,
                                         String writer_image,
                                         String time,
                                         int timestamp,
                                         float rating,
                                         String contents,
                                         int recommend,
                                         int totalCount) {

        if (database != null) {


            String sql = "insert or replace into comment( id, writer, movieId,  writer_image,  time,  timestamp,  rating,  contents,  recommend,totalCount)values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {id, writer, movieId, writer_image, time, timestamp, rating, contents, recommend,totalCount};

            database.execSQL(sql, params);

        }
    }


    public static ArrayList<CommentVo> selectCommentList(int key) {
        ArrayList<CommentVo> list = new ArrayList<CommentVo>();

        if (database != null) {
            String sql = "select * from comment where movieId=" + key;
            Cursor cursor = database.rawQuery(sql, null);

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                int id = cursor.getInt(0);
                String writer = cursor.getString(1);
                int movieId = cursor.getInt(2);
                String writer_image = cursor.getString(3);
                String time = cursor.getString(4);
                int timestamp = cursor.getInt(5);
                float rating = cursor.getFloat(6);
                String contents = cursor.getString(7);
                int recommend = cursor.getInt(8);
                int totalCount = cursor.getInt(9);

                list.add(new CommentVo(id,
                        writer,
                        movieId,
                        writer_image,
                        time,
                        timestamp,
                        rating,
                        contents,
                        recommend,
                        totalCount));

            }

            cursor.close();
        }
        return list;
    }



}
