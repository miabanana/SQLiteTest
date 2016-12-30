package com.huayinghuang.sqlitetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by miahuang on 2016/2/22.
 */
public class MyDBOpenHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "people";

    public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + " (_id INTEGER PRIMARY KEY,"
                + "name TEXT NOT NULL,"
                + "number TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DROP_TABLE = "DROP TABLE" + TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);

    }
}
