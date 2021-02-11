package com.cryptofication.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseClass extends SQLiteOpenHelper {

    private String SQLiteCreate = "CREATE TABLE Favorites (ID_FAVORITE NUMBER PRIMARY KEY, ID_CRYPTO_1 TEXT, ID_CRYPTO_2 TEXT)"; //Create table query

    public DatabaseClass(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLiteCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}