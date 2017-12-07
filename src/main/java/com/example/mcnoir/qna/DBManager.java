package com.example.mcnoir.qna;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.sql.SQLException;
import java.util.ArrayList;


public class DBManager {

    //columns for questions table
    private static final String DATABASE_TABLE2 = "Questions";
    public static final String KEY_MSG_ID = "msg_id";
    public static final String KEY_MESSAGE_TXT = "message_txt";
    public static final String MESSAGE_P = "message_point";


    //Question table query
    private static final String DATABASE_CREATE_QUEST =
            "create table if not exists Questions(msg_id integer primary key autoincrement, " +
                    "message_txt text not null, " + "message_point real" + ");";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NewAppQuestDB";



    //other attributes
    private final Context context;
    private MyDatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBManager(Context ctx) {
        this.context = ctx;
        DBHelper = new MyDatabaseHelper(context);
    }

    private static class MyDatabaseHelper extends SQLiteOpenHelper {

        MyDatabaseHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_QUEST);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //clear all data
            db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE2);

            onCreate(db);
        }

    }// END INNER CLASS

    public SQLiteDatabase open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return db;
    }

    public void close() {
        DBHelper.close();

    }


    public long insertMSG(String message_txt, double message_point) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_MESSAGE_TXT, message_txt);
        initialValues.put(MESSAGE_P, message_point);
        return db.insert(DATABASE_TABLE2, null, initialValues);
    }

    public boolean updatedMsg(long rowId, double message_p)
    {
        String where = KEY_MSG_ID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(MESSAGE_P, message_p);

        return db.update(DATABASE_TABLE2, newValues, where, null) > 0;
    }


    public boolean deleteMsg(long rowId) throws SQLException
    {
        String where = KEY_MSG_ID + "=" + rowId;

        return db.delete(DATABASE_TABLE2, where, null) > 0; // !=0
    }

    public Cursor alterIds()
    {
        String query = "drop table if exists "+ DATABASE_TABLE2;
        Cursor mCursor = db.rawQuery(query, null);
        return  mCursor;
    }

    public Cursor getRow(long rowId) {
        String where = KEY_MSG_ID + "=" + rowId;

        Cursor c = db.query(true, DATABASE_TABLE2, new String[]{
                KEY_MSG_ID, KEY_MESSAGE_TXT}, where, null, null, null, null, null);

        return  c;
    }

    public Cursor getRow2() {
        String query = "select msg_id from "+ DATABASE_TABLE2;
        Cursor mCursor = db.rawQuery(query, null);
        if(mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return  mCursor;
    }

    public Cursor getPoints(long points) {
        String query = "select message_point from "+ DATABASE_TABLE2 + " where msg_id = " + points;
        Cursor mCursor = db.rawQuery(query, null);
        if(mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return  mCursor;
    }



    public Cursor getAllMsgs()
    {
        //return db.query(true, DATABASE_TABLE2, new String[]{
        //KEY_MSG_ID, KEY_MESSAGE_TXT},null, null, null, null, null, null);
        String query = "select msg_id, message_txt, message_point from "+ DATABASE_TABLE2;
        Cursor mCursor = db.rawQuery(query, null);
        if(mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return  mCursor;
    }


}
