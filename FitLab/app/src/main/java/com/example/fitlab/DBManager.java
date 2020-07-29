package com.example.fitlab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;

public class DBManager extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "FitLabDB.db";
    public static final String TABLE_NAME1 = "SessionList";
    public static final String C_ID = BaseColumns._ID;
    public static final String C_NAME = "LIST_NAME";
    public static final String C_DATE = "CREATED_DATE";
    public static final String C_TIME = "ELAPSED_TIME";
    public static final String C_CAL = "CALORIES_CALC";
    public static final String C_TOT = "TOTAL_LAPS";
    public static final String TABLE_NAME2 = "LapList";
    public static final String C_LIST = "LIST_NAME";
    public static final String C_LAP = "LAP_NUMBER";
    public static final String C_COUNT = "LAP_TIME";
    public static final String TABLE_NAME3 = "ProfileList";
    public static final String C_USER = "USER_NAME";
    public static final String C_WEIGHT = "USER_WEIGHT";

    public DBManager(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String executeSql1 = "create table " + TABLE_NAME1 + " (" + C_ID + " INTEGER PRIMARY KEY, "
                + C_NAME + " text, " + C_DATE + " text, " + C_TIME + " text, " + C_CAL + " text, " + C_TOT + " int)";
        String executeSql2 = "create table " + TABLE_NAME2 + " (" + C_ID + " INTEGER PRIMARY KEY, "
                + C_LIST + " text, " + C_LAP + " int, " + C_COUNT + " text)";
        String executeSql3 = "create table " + TABLE_NAME3 + " (" + C_ID + " INTEGER PRIMARY KEY, "
                + C_USER + " text, " + C_WEIGHT + " int)";

        sqLiteDatabase.execSQL(executeSql1);
        sqLiteDatabase.execSQL(executeSql2);
        sqLiteDatabase.execSQL(executeSql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME1);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME2);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME3);
        onCreate(sqLiteDatabase);
    }

    public long createLap(Lap lap)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_LIST, lap.getListName());
        values.put(C_LAP, lap.getLapNumber());
        values.put(C_COUNT, lap.getLapTime());

        long lapId = db.insert(TABLE_NAME2, null, values);
        return lapId;
    }

    public long createProfile(Profile profile)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_USER, profile.getUserName());
        values.put(C_WEIGHT, profile.getUserWeight());
        long profileId = db.insert(TABLE_NAME3, null, values);
        return profileId;
    }

    public long createSession(Session session)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_NAME, session.getListName());
        values.put(C_DATE, getDateTime());
        long sessionId = db.insert(TABLE_NAME1, null, values);
        return sessionId;
    }

    public void updateSession(int sessionId, long elapsedTime, int totalLaps, String calorieCalc)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_TIME, elapsedTime);
        values.put(C_TOT, totalLaps);
        values.put(C_CAL, calorieCalc);

        db.update(TABLE_NAME1, values, C_ID + " = ?", new String[] { String.valueOf(sessionId) });
    }

    public Session getSession(int sessionId)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME1 + " WHERE "
                + C_ID + " = " + sessionId;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Session session = new Session();
        session.setId(c.getInt(c.getColumnIndex(C_ID)));
        session.setListName(c.getString(c.getColumnIndex(C_NAME)));
        session.setCreatedDate(c.getString(c.getColumnIndex(C_DATE)));
        session.setElapsedTime(c.getLong(c.getColumnIndex(C_TIME)));
        session.setCaloriesCalc(c.getString(c.getColumnIndex(C_CAL)));
        session.setTotalLaps(c.getInt(c.getColumnIndex(C_TOT)));

        return session;
    }

    public List<Profile> getProfiles()
    {
        List<Profile> allProfiles = new ArrayList<Profile>();
        String selectQuery = "SELECT * FROM ProfileList";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do {
                Profile profile = new Profile();
                profile.setId(cursor.getInt(cursor.getColumnIndex(C_ID)));
                profile.setUserName(cursor.getString(cursor.getColumnIndex(C_USER)));
                profile.setUserWeight(cursor.getInt(cursor.getColumnIndex(C_WEIGHT)));
                allProfiles.add(profile);
            }while (cursor.moveToNext());
        }
        return allProfiles;
    }

    public List<Session> getSessions()
    {
        List<Session> allSessions = new ArrayList<Session>();
        String selectQuery = "SELECT * FROM SessionList";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do {
                Session session = new Session();
                session.setId(cursor.getInt((cursor.getColumnIndex(C_ID))));
                session.setListName(cursor.getString(cursor.getColumnIndex(C_NAME)));
                allSessions.add(session);
            }while (cursor.moveToNext());
        }
        return allSessions;
    }

    public int totalLapCount(String listName)
    {
        List<Lap> laps = new ArrayList<Lap>();
        int totalAmount = 0;

        String selectQuery = "SELECT * FROM LapList WHERE LIST_NAME = ?";
        String[] selectionArgs = {listName};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

        if (cursor.moveToFirst())
        {
            do {
                Lap lap = new Lap();
                lap.setId(cursor.getInt(cursor.getColumnIndex(C_ID)));
                lap.setListName(cursor.getString(cursor.getColumnIndex(C_LIST)));
                lap.setLapNumber(cursor.getInt(cursor.getColumnIndex(C_LAP)));
                lap.setLapTime(cursor.getLong(cursor.getColumnIndex(C_COUNT)));
                laps.add(lap);
            }while (cursor.moveToNext());
        }
        for (Lap lap1 : laps) {
            int lapCount = 1;
            totalAmount += lapCount;
        }
        return totalAmount;
    }

    private String getDateTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

//    public int totalLapTime(String listName)
//    {
//        List<Lap> laps = new ArrayList<Lap>();
//        int totalAmount = 0;
//
//        String selectQuery = "SELECT * FROM LapList WHERE LIST_NAME = ?";
//        String[] selectionArgs = {listName};
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);
//
//        if (cursor.moveToFirst())
//        {
//            do {
//                Lap lap = new Lap();
//                lap.setId(cursor.getInt(cursor.getColumnIndex(C_ID)));
//                lap.setListName(cursor.getString(cursor.getColumnIndex(C_LIST)));
//                lap.setLapNumber(cursor.getInt(cursor.getColumnIndex(C_LAP)));
//                lap.setLapTime(cursor.getLong(cursor.getColumnIndex(C_COUNT)));
//                laps.add(lap);
//            }while (cursor.moveToNext());
//        }
//        for (Lap lap1 : laps) {
//            int lapTime = (int) lap1.getLapTime();
//            totalAmount += lapTime;
//        }
//        return totalAmount;
//    }

}
