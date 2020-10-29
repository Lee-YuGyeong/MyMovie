package com.example.movie;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class OutlineDatabase {

    private static final String TAG = "AppHelper";

    private static SQLiteDatabase database;

    private static String createTableOutlineSql = "create table if not exists outline " +
            "(" +
            "    id integer PRIMARY KEY, " +
            "    title text, " +
            "    title_eng text, " +
            "    dateValue text, " +
            "    user_rating float, " +
            "    audience_rating float, " +
            "    reviewer_rating float, " +
            "    reservation_rate float, " +
            "    reservation_grade integer, " +
            "    grade integer, " +
            "    thumb text, " +
            "    image text " +
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
            if (tableName.equals("outline")) {
                database.execSQL(createTableOutlineSql);
                println("outline 테이블 생성 요청됨.");
            }
        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }
    }

    public static void insertOutlineData(int id,
                                         String title,
                                         String title_eng,
                                         String dateValue,
                                         float user_rating,
                                         float audience_rating,
                                         float reviewer_rating,
                                         float reservation_rate,
                                         int reservation_grade,
                                         int grade,
                                         String thumb,
                                         String image) {
        println("insertOutlineData() 호출됨.");

        if (database != null) {


            String sql = "insert or replace into outline( id, title, title_eng,  dateValue,  user_rating,  audience_rating,  reviewer_rating,  reservation_rate,  reservation_grade,  grade,  thumb,  image)values(?,?,?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {id, title, title_eng,dateValue, user_rating,audience_rating,reviewer_rating,reservation_rate,reservation_grade,grade,thumb,image};

            database.execSQL(sql, params);
            //   database.execSQL(sql);

            println("데이터 추가함.");
        } else {
            println("먼저 데이터베이스를 오픈하세요.");
        }

    }

    public static ArrayList<MovieVo> selectOutlineList(){
        println("selectData() 호출됨.");
        ArrayList<MovieVo> list = new ArrayList<MovieVo>();


        if (database != null) {
            String sql = "select * from outline"; //from 다음에 공백 꼭 입력
            Cursor cursor = database.rawQuery(sql, null);


            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String title_eng = cursor.getString(2);
                String dateValue = cursor.getString(3);
                float user_rating = cursor.getFloat(4);
                float audience_rating = cursor.getFloat(5);
                float reviewer_rating = cursor.getFloat(6);
                float reservation_rate = cursor.getFloat(7);
                int reservation_grade = cursor.getInt(8);
                int grade = cursor.getInt(9);
                String thumb = cursor.getString(10);
                String image = cursor.getString(11);

                list.add(new MovieVo(id,
                        title,
                        title_eng,
                        dateValue,
                        user_rating,
                        audience_rating,
                        reviewer_rating,
                        reservation_rate,
                        reservation_grade,
                        grade,
                        thumb,
                        image));

            }

            cursor.close();
        }
        return list;
    }


    public static void println(String data) {
        Log.d(TAG, data);
    }
}
