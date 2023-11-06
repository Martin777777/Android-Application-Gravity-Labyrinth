package com.example.user;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_ACCOUNT = "create table account ("
            + "id integer primary key autoincrement, "
            + "username text, "
            + "password text, "
            + "email text, "
            + "phone_number text, "
            + "gender text)";

    public static final String CREATE_SCORE = "create table score ("
            + "id integer primary key autoincrement, "
            + "username text, "
            + "level integer, "
            + "time integer)";

    private Context context;

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ACCOUNT);
        sqLiteDatabase.execSQL(CREATE_SCORE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
