package com.cryptofication.classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseClass extends SQLiteOpenHelper {

    private final String SQLiteCreateTableFavorites = "CREATE TABLE Favorites (SYMBOL TEXT PRIMARY KEY, POSITION TEXT)";
    private final String SQLiteCreateTableConversions = "CREATE TABLE Conversions (ID_CONVERSION NUMBER PRIMARY KEY, ID_CRYPTO_1 TEXT, ID_CRYPTO_2 TEXT)";
    private final String SQLiteDropTableFavorites = "DROP TABLE IF EXISTS 'Favorites'";
    private final String SQLiteDropTableConversions = "DROP TABLE IF EXISTS 'Conversions'";

    public DatabaseClass(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLiteCreateTableFavorites);
        db.execSQL(SQLiteCreateTableConversions);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQLiteDropTableFavorites);
        db.execSQL(SQLiteDropTableConversions);
        onCreate(db);
    }

    public int searchInTableFavorites(String id_favorite) {
        SQLiteDatabase db_search = this.getReadableDatabase();
        String[] fields = new String[]{"ID_FAVORITE"};
        String[] args = new String[]{id_favorite};
        Cursor res = db_search.query("Favorites", fields, //Search in database
                "Username = ?", args, null, null, null);
        if (res.getCount() >= 1) {
            res.moveToNext();
            db_search.close();
            return 0;
        } else {
            db_search.close();
            return 1;
        }
    }
}