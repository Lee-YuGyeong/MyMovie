package com.example.movie.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.movie.database.MovieDetailVo;

import java.util.ArrayList;

public class MovieDetailDatabase {
    private static final String TAG = "AppHelper";

    private static SQLiteDatabase database;

    private static String createTableDetailSql = "create table if not exists detail " +
            "(" +
            "    id integer PRIMARY KEY, " +
            "    title text, " +
            "    dateValue text, " +
            "    user_rating float, " +
            "    audience_rating float, " +
            "    reviewer_rating float, " +
            "    reservation_rate float, " +
            "    reservation_grade integer, " +
            "    grade integer, " +
            "    thumb text, " +
            "    image text, " +
            "    photos text, " +
            "    videos text, " +
            "    outlinks text, " +
            "    genre text, " +
            "    duration integer, " +
            "    audience integer, " +
            "    synopsis text, " +
            "    director text, " +
            "    actor text, " +
            "    _like integer, " +
            "    dislike integer " +
            ")";

    public static void openDatabase(Context context, String databaseName) {
        println("openDatabase 호출됨");
        try {
            database = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
            if (database != null) {
                println("데이터베이스 " + databaseName + "오픈됨.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTable(String tableName) {
        println("createTable 호출됨 : " + tableName);

        if (database != null) {
            if (tableName.equals("detail")) {
                database.execSQL(createTableDetailSql);
                println("detail 테이블 생성 요청됨.");
            }
        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }
    }

    public static void insertDetailData(int id,
                                        String title,
                                        String dateValue,
                                        float user_rating,
                                        float audience_rating,
                                        float reviewer_rating,
                                        float reservation_rate,
                                        int reservation_grade,
                                        int grade,
                                        String thumb,
                                        String image,
                                        String photos,
                                        String videos,
                                        String outlinks,
                                        String genre,
                                        int duration,
                                        int audience,
                                        String synopsis,
                                        String director,
                                        String actor,
                                        int _like,
                                        int dislike) {
        println("insertDetailData() 호출됨.");

        if (database != null) {

            String sql = "insert or replace into detail(id, title,  dateValue,  user_rating,  audience_rating,  reviewer_rating,  reservation_rate,  reservation_grade,  grade,  thumb,  image,photos,videos,outlinks,genre,duration,audience,synopsis,director,actor,_like,dislike)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {id, title, dateValue, user_rating, audience_rating, reviewer_rating, reservation_rate, reservation_grade, grade, thumb, image, photos, videos, outlinks, genre, duration, audience, synopsis, director, actor, _like, dislike};

            database.execSQL(sql, params);

            println("데이터 추가함.");
        } else {
            println("먼저 데이터베이스를 오픈하세요.");
        }

    }

    public static ArrayList<MovieDetailVo> selectDetailList() {
        println("selectData() 호출됨.");
        ArrayList<MovieDetailVo> list = new ArrayList<MovieDetailVo>();


        if (database != null) {
            String sql = "select * from detail"; //from 다음에 공백 꼭 입력
            Cursor cursor = database.rawQuery(sql, null);


            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String dateValue = cursor.getString(2);
                float user_rating = cursor.getFloat(3);
                float audience_rating = cursor.getFloat(4);
                float reviewer_rating = cursor.getFloat(5);
                float reservation_rate = cursor.getFloat(6);
                int reservation_grade = cursor.getInt(7);
                int grade = cursor.getInt(8);
                String thumb = cursor.getString(9);
                String image = cursor.getString(10);
                String photos = cursor.getString(11);
                String videos = cursor.getString(12);
                String outlinks = cursor.getString(13);
                String genre = cursor.getString(14);
                int duration = cursor.getInt(15);
                int audience = cursor.getInt(16);
                String synopsis = cursor.getString(17);
                String director = cursor.getString(18);
                String actor = cursor.getString(19);
                int _like = cursor.getInt(20);
                int dislike = cursor.getInt(21);

                list.add(new MovieDetailVo(id,
                        title,
                        dateValue,
                        user_rating,
                        audience_rating,
                        reviewer_rating,
                        reservation_rate,
                        reservation_grade,
                        grade,
                        thumb,
                        image,
                        photos,
                        videos,
                        outlinks,
                        genre,
                        duration,
                        audience,
                        synopsis,
                        director,
                        actor,
                        _like,
                        dislike));

            }

            cursor.close();
        }
        return list;
    }


    public static void println(String data) {
        Log.d(TAG, data);
    }
}
