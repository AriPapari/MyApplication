package com.gecaj.arianit.myapplication.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gecaj.arianit.myapplication.colorpicker.ColorActivity;


/**
 * Created by Arianit on 22.11.2016.
 */

public class DBHandler extends SQLiteOpenHelper{

    private static final String LOG_TAG = ColorActivity.class.toString();
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "colorList.db";
    public static final String TABLE_COLORS = "colorList";
    public static final String TABLE_EFFECT = "effect";
    public static final String COLUMN_HEXCOLOR ="_hexcolor";
    public static final String COLUMN_SPEED ="_speed";
    public static final String COLUMN_EFFECT ="_effect";
    public static final String COLUMN_POS ="_pos";
    public static final String COLUMN_RED ="_red";
    public static final String COLUMN_GREEN ="_green";
    public static final String COLUMN_BLUE ="_blue";
    public static final String COLUMN_WHITE ="_white";
    public static final String COLUMN_BRIGHT ="_brightness";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Creating Table for RGB Colors
        String query = "CREATE TABLE " + TABLE_COLORS + "(" +
                COLUMN_POS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RED + " DOUBLE, " +
                COLUMN_GREEN + " DOUBLE, " +
                COLUMN_BLUE + " DOUBLE, " +
                COLUMN_WHITE + " DOUBLE, " +
                COLUMN_BRIGHT + " DOUBLE " +
                ")";
        sqLiteDatabase.execSQL(query);
        //Creating Table for Effects
        query = "CREATE TABLE " + TABLE_EFFECT + "(" +
                COLUMN_HEXCOLOR + " CHAR(6), " +
                COLUMN_SPEED + " DOUBLE, " +
                COLUMN_EFFECT + " INT " +
                ")";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_COLORS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EFFECT);
        onCreate(sqLiteDatabase);
    }

    //Truncate Table and add Colors
    public void addHEXcolor(String[] hxc, double speed, int effect){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("TRUNCATE TABLE " + TABLE_EFFECT + ";");
        ContentValues values = new ContentValues();
        for(int i = 0; i <= hxc.length; i++)
            values.put(COLUMN_HEXCOLOR,hxc[i]);
        values.put(COLUMN_SPEED, speed);
        values.put(COLUMN_EFFECT, effect);
        db.insert(TABLE_EFFECT, null, values);
    }

    public void setColumnSpeed(double speed){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_EFFECT+
                " SET "+COLUMN_SPEED+" = "+speed+
                " WHERE "+COLUMN_SPEED+" != 0;");
    }

    public void setColumnEffect(int effect){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_EFFECT+
                " SET "+COLUMN_EFFECT+" = "+effect+
                " WHERE "+COLUMN_EFFECT+" != 0;");
    }

    //Add Color to db
    public void addRGBcolor(double[] rgb){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RED, rgb[0]);
        values.put(COLUMN_GREEN, rgb[1]);
        values.put(COLUMN_BLUE, rgb[2]);
        values.put(COLUMN_WHITE, rgb[3]);
        values.put(COLUMN_BRIGHT, rgb[4]);

        db.insert(TABLE_COLORS, null, values);
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

    //get rgb Colors
    public Cursor getAllRGBdata(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_COLORS ,null);
        return result;
    }

    public Cursor getAllHEXdata(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT "+COLUMN_HEXCOLOR+" FROM " + TABLE_EFFECT ,null);
        return result;
    }

    /*
    public void dropIt(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE "+TABLE_COLORS);
    }
    */
}
