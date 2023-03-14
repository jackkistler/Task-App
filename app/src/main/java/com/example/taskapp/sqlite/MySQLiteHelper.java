package com.example.taskapp.sqlite;//package com.example.taskapp2021.sqlite;//package com.example.taskapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "MySQLiteHelper";
    private static final String DATA_BASE_NAME = "tasks.sqlite";
    private static final int DATABASE_VERSION = 1;

    // Note that when getWritableDatabase() gets called (it's inherited), Android will check to see
    // if the database file (tasks.sqlite) has been created.
    // It will invoke onCreate to create database if it has not already been created
    // If the database has already been created, then Android will
    // look at the DATABASE_VERSION param
    // and compare it to the the version number in samples.db
    // If the param (DATABASE_VERSION) is greater than the version number
    // in samples.db, then Android will call onUpgrade
    public MySQLiteHelper(Context context){
        super(context, DATA_BASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String taskTableSQL = SQLTaskDataAccess.TABLE_CREATE;
        db.execSQL(taskTableSQL);
        Log.d(TAG, taskTableSQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(TAG, String.format("New version of the database, updating from version %d to version %d", oldVersion, newVersion));
        Log.d(TAG,"Run the DDL statements here, but be careful not to lose any user data!!!!!");

        // There is a strategy for upgrading the database, but it's complicated
        // if your app has already been distributed to users.
        // You have to be careful to upgrade the database so
        // that when users update your app, their data is not corrupted

        // NOTE THAT THE ONLY CASE WITH A BREAK STATEMENT IS THE LAST ONE
        switch(oldVersion) {
            case 1:
               Log.d(TAG, "Upgrade logic from version 1 to 2 (ALTER existing tables and create new ones)");
               String addColumnSQL = "ALTER TABLE BLAH ADD someNewColumn VARCHAR;";
               db.execSQL(addColumnSQL);
            case 2:
               Log.d(TAG, "Upgrade logic from version 2 to 3 (ALTER existing tables and create new ones)");
            case 3:
               Log.d(TAG, "Upgrade logic from version 3 to 4 (ALTER existing tables and create new ones)");
               break;
            default:
               throw new IllegalStateException(
                        "onUpgrade() with unknown oldVersion " + oldVersion);
        }

    }
}