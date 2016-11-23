package com.gecaj.arianit.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by Arianit on 22.11.2016.
 */

public class DBHandler extends SQLiteOpenHelper{

    private static final String LOG_TAG = ColorActivity.class.toString();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "colorList.db";
    public static final String TABLE_COLORS = "colorList";
    public static final String COLUMN_POS ="_pos";
    public static final String COLUMN_RED ="_red";
    public static final String COLUMN_GREEN ="_green";
    public static final String COLUMN_BLUE ="_blue";
    public static final String COLUMN_WHITE ="_white";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_COLORS + "(" +
                COLUMN_POS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RED + " INTEGER, " +
                COLUMN_GREEN + " INTEGER, " +
                COLUMN_BLUE + " INTEGER, " +
                COLUMN_WHITE + " INTEGER " +
                ")";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_COLORS);
        onCreate(sqLiteDatabase);
    }

    //Add Color to db
    public void addColor(int[] rgb){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RED, rgb[0]);
        values.put(COLUMN_GREEN, rgb[1]);
        values.put(COLUMN_BLUE, rgb[2]);
        values.put(COLUMN_WHITE, rgb[3]);

        long result = db.insert(TABLE_COLORS, null, values);
        Log.i(LOG_TAG, "++++++ COLOR R"+rgb[0]+" G"+rgb[1]+" B"+rgb[2]+" W"+rgb[3]+" ADDED +++++++ result = "+ result);
    }

    //Delete all Data
    public void deleteData(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_COLORS + ";");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='"+TABLE_COLORS+"';");
        Log.i(LOG_TAG, "-------- TABLE DELETED ------");
    }

    /*/Delete Color from db
    public void deleteColor(int pos){
        Log.i(LOG_TAG, "-------- delete COLOR called ------");
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_COLORS + " WHERE " + COLUMN_POS + "=" + pos + ";");
        Log.i(LOG_TAG, "-------- COLOR DELETED ------");
    }*/

    //get Cursor/allData
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_COLORS ,null);
        return result;
    }

    /*
    public void dropIt(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE "+TABLE_COLORS);
    }
    */
}
