package com.siehuai.smartdrugbox.controller;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.siehuai.smartdrugbox.data.AlarmData;
import com.siehuai.smartdrugbox.data.AlarmDataList;
import com.siehuai.smartdrugbox.data.DataBaseContract;
import com.siehuai.smartdrugbox.data.MyTime;

import java.util.ArrayList;


public class PostsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "postsDatabase";
    private static final int DATABASE_VERSION = 1;

    private static PostsDatabaseHelper sInstance;

    public static synchronized PostsDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new PostsDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    private PostsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @TargetApi(16)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERS_TABLE = "CREATE TABLE " + DataBaseContract.AlarmEntry.TABLE_NAME +
                "(" +
                DataBaseContract.AlarmEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                DataBaseContract.AlarmEntry.COLUMN_NAME_A_HOUR + " INTEGER," +
                DataBaseContract.AlarmEntry.COLUMN_NAME_A_MINUTE + " INTEGER," +
                DataBaseContract.AlarmEntry.COLUMN_NAME_STATUS + " INTEGER" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.AlarmEntry.TABLE_NAME);
            onCreate(db);
        }
    }

    /*
    Need to set db.setTransactionSuccessful to successful update
     */
    public int addOrUpdateAlarmFrmDb(AlarmData alarmData) {

        SQLiteDatabase db = getWritableDatabase();
        MyTime myTime = alarmData.getMyTime();
        db.beginTransaction();
        int rows = 0;
        long alarmId = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(DataBaseContract.AlarmEntry.COLUMN_NAME_A_HOUR, myTime.getHour());
            values.put(DataBaseContract.AlarmEntry.COLUMN_NAME_A_MINUTE, myTime.getMinute());
            values.put(DataBaseContract.AlarmEntry.COLUMN_NAME_STATUS, alarmData.isStatus());

            rows = db.update(DataBaseContract.AlarmEntry.TABLE_NAME,
                    values, DataBaseContract.AlarmEntry.COLUMN_NAME_ID + "= ?",
                    new String[]{String.valueOf(alarmData.getAlarmID())});
            if (rows > 0) {
                db.setTransactionSuccessful();
                return rows;
            } else {
                alarmId = db.insertOrThrow(DataBaseContract.AlarmEntry.TABLE_NAME, null, values);
                alarmData.setAlarmID(alarmId);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
        } finally {
            db.endTransaction();
        }
        return rows;
    }

    public void addOrUpdateAlarmLocal(AlarmData alarmData, boolean result) {
        if (result) {
            updateAlarmData(alarmData);
        } else {
            AlarmDataList.mAlarmDataList.add(alarmData);
        }

    }

    public ArrayList<AlarmData> getAllAlarmFrmDb() {


        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s", DataBaseContract.AlarmEntry.TABLE_NAME);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        ArrayList<AlarmData> alarmDataArrayList = new ArrayList<AlarmData>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    int hour = cursor.getInt(cursor.getColumnIndex(DataBaseContract.AlarmEntry.COLUMN_NAME_A_HOUR));
                    int minute = cursor.getInt(cursor.getColumnIndex(DataBaseContract.AlarmEntry.COLUMN_NAME_A_MINUTE));
                    int id = cursor.getInt(cursor.getColumnIndex(DataBaseContract.AlarmEntry.COLUMN_NAME_ID));
                    int status = cursor.getInt(cursor.getColumnIndex(DataBaseContract.AlarmEntry.COLUMN_NAME_STATUS));
                    MyTime myTime = new MyTime(hour, minute);
                    AlarmData alarmData = new AlarmData(myTime, status, id);

                    alarmDataArrayList.add(alarmData);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return alarmDataArrayList;
    }

    public boolean deleteAlarmFromDb(int alarmId) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        int result = 0;
        try {
            result = db.delete(DataBaseContract.AlarmEntry.TABLE_NAME,
                    DataBaseContract.AlarmEntry.COLUMN_NAME_ID + "= ?",
                    new String[]{String.valueOf(alarmId)});
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d("PostDatabase", "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
            Log.d("PostDatabase + Delete", String.valueOf(result));
            return (result == 0 ? false : true);
        }
    }

    public void notifyAdapterDataChange(ReminderListViewAdapter listAdapter) {
        listAdapter.notifyDataSetChanged();
    }

    public void updateAlarmInLocal(ArrayList<AlarmData> alarmArrayList) {
        //TODO: Set up a service that get alarmData
        AlarmDataList.mAlarmDataList = alarmArrayList;
    }

    private void updateAlarmData(AlarmData alarmData) {
        for (AlarmData mAlarmData : AlarmDataList.mAlarmDataList) {
            if (alarmData.getAlarmID() == mAlarmData.getAlarmID()) {
                mAlarmData = alarmData;
            }
        }
    }

}


