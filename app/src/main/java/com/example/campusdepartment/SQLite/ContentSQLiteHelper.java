package com.example.campusdepartment.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;


public class ContentSQLiteHelper extends SQLiteOpenHelper {
    public static final String cREATE_BOOK = "CREATE table commodities("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "picture blob,"
            + "content VARCHAR(200),"
            + "price VARCHAR(20),"
            + "number VARCHAR(200),"
            + "user_phone VARCHAR(20))";
    public SQLiteDatabase sqLiteDatabase;
    Drawable bitmap;
    private String CREATE_RECORDS_TABLE = "create table table_records(_id integer primary key autoincrement,"
            + "username VARCHAR(20),"
            + "image blob,"
            + "number VARCHAR(20),"
            + "number1 VARCHAR(20),"
            + "author VARCHAR(20))";

    public ContentSQLiteHelper(Context context) {
        super(context, "records_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(cREATE_BOOK);
        db.execSQL(CREATE_RECORDS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
