package com.rasoftec.tpos2.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.rasoftec.tpos2.Data.database.DATABASE_NAME;

public class Queries extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "basetar760.db";

    public Queries(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
