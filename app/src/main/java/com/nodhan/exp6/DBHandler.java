package com.nodhan.exp6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nodhan on 23/10/16.
 */

public class DBHandler extends SQLiteOpenHelper {

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table birthday('id' integer primary key, 'name' varchar(30), 'day' integer, 'month' integer, 'year' integer, 'phone' varchar(12))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists birthday");
        onCreate(db);
    }


    /**
     * Inserts data into SQLite DB
     *
     * @param name name of the person
     * @param dates int array (date, month, year)
     * @param mobileNumber phone number of person
     */
    public void add(String name, int[] dates, String mobileNumber) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("day", dates[0]);
        contentValues.put("month", dates[1]);
        contentValues.put("year", dates[2]);
        contentValues.put("phone", mobileNumber);

        sqLiteDatabase.insert("birthday", null, contentValues);
        sqLiteDatabase.close();
    }

    /**
     * Selects all the data in database
     * @return cursor object
     */
    public Cursor display() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from birthday", null);
    }

    /**
     * Selects the data of the person with id
     * @param id id of the person
     * @return cursor object containing the person's data
     */
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM birthday WHERE id=?", new String[]{id+""});
    }
}
