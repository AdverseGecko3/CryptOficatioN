package com.cryptofication.classes;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseClass extends SQLiteOpenHelper {

    private final String SQLiteCreateTableFavorites = "CREATE TABLE Favorites (SYMBOL TEXT PRIMARY KEY, DATE_ADDED TEXT)";
    private final String SQLiteDropTableFavorites = "DROP TABLE IF EXISTS 'Favorites'";

    public DatabaseClass(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLiteCreateTableFavorites);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQLiteDropTableFavorites);
        onCreate(db);
    }

    public int insertToFavorites(String symbol, String date) {
        SQLiteDatabase writable, readable;
        writable = this.getWritableDatabase();
        readable = this.getReadableDatabase();
        // Insert both entered fields into the database
        String query = "INSERT INTO Favorites VALUES ('" + symbol + "' , '" + date + "')";
        boolean unique = true;

        try {
            writable.execSQL(query);
        } catch (Exception e) {
            e.getStackTrace();
            unique = false;
        }

        String[] fields = new String[]{"symbol"};
        String[] args = new String[]{symbol};

        Cursor cursor = readable.query("Favorites", fields, //Search in database
                "symbol = ?", args, null, null, null);

        int i = cursor.getCount();
        writable.close();
        readable.close();
        cursor.close();
        Log.d("insertFavorites", "Cryptos with that symbol in favorites: " + i);
        if (!unique) {
            return -1;
        } else if (i == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    public int deleteFromFavorites(String symbol) {
        SQLiteDatabase writable;
        writable = this.getWritableDatabase();
        String[] val = new String[]{symbol};

        int sol = writable.delete("Favorites", "symbol = ?", val); //Delete from table
        writable.close();
        return sol;
    }

    public float searchInFavorites(String username) {
        SQLiteDatabase readable = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM Rates WHERE username = '" + username + "'";
        try {
            Cursor cursor = readable.rawQuery(selectQuery, null);
            if (cursor.getCount() >= 1) {
                Log.d("ratingQuery", "User already voted");
                cursor.moveToNext();
                float sol = cursor.getFloat(1);
                readable.close();
                cursor.close();
                return sol;
            } else {
                Log.d("ratingQuery", "User did not vote");
                readable.close();
                cursor.close();
                return -5;
            }
        } catch (SQLException ex) {
            Log.d("ratingQuery", "Exception");
            return -5;
        }
    }
}