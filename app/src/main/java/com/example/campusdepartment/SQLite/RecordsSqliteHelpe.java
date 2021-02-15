package com.example.campusdepartment.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 林嘉煌 on 2020/5/3.
 */

public class RecordsSqliteHelpe extends SQLiteOpenHelper {

    private String CREATE_RECORDS_TABLE = "create table table_records(_id integer primary key autoincrement,"
            + "username VARCHAR(20),"
            + "image blob,"
            + "number VARCHAR(20),"
            + "number1 VARCHAR(20),"
            + "author VARCHAR(20))";

    public RecordsSqliteHelpe(Context context) {
        super(context, "records_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_RECORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
