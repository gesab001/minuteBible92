package com.example.minutebible9;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    public DatabaseHelper(Context context, String db_name) {
        super(context, db_name, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //searches the bible in the database based on the keyword
    public Cursor searchData(String tableName, String word) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + tableName + " where WORD LIKE "  +"'%" + word+"%'"  , null);

        return res;

    }

    //gets a verse from the bible
    public Cursor getAVerse(String tableName, int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + tableName + " where ID= " + id , null);

        return res;

    }

    //gets all the titles of the hymns in the database
    public Cursor getAllHymnTitles(String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + tableName, null);

        return res;

    }
}
