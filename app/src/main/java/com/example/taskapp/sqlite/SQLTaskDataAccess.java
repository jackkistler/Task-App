package com.example.taskapp.sqlite;//package com.example.taskapp2021.sqlite;//package com.example.taskapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

//import com.example.taskapp2021.Taskable;
//import com.example.taskapp2021.models.Task;

import com.example.taskapp.Taskable;
import com.example.taskapp.models.Task;

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLTaskDataAccess implements Taskable {

    public static final String TAG = "SQLTaskDataAccess";

    // Instance variables
    private ArrayList<Task> allTasks;
    private Context context;
    private MySQLiteHelper dbHelper;
    private SQLiteDatabase database;
    SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");

    // Constructors
    public SQLTaskDataAccess(Context context){
        Log.d(TAG, "Instantiating SQLDataAccess");
        this.context = context;
        this.dbHelper = new MySQLiteHelper(context);
        this.database = this.dbHelper.getWritableDatabase();
    }


    // We should create static constants for the table name, and all column names
    public static final String TABLE_NAME = "tasks";
    public static final String COLUMN_TASK_ID = "_id";
    public static final String  COLUMN_DESCRIPTION = "description";
    public static final String  COLUMN_DUE = "due";
    public static final String  COLUMN_DONE = "done";

    public static final String TABLE_CREATE = String.format("create table %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER )",
            TABLE_NAME,
            COLUMN_TASK_ID,
            COLUMN_DESCRIPTION,
            COLUMN_DUE,
            COLUMN_DONE
    );


    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        String query = String.format("SELECT %s, %s, %s, %s FROM %s", COLUMN_TASK_ID, COLUMN_DESCRIPTION, COLUMN_DUE, COLUMN_DONE, TABLE_NAME);

        return tasks;
    }

    @Override
    public Task getTaskById(long id) {
        String query = String.format("SELECT %s, %s, %s, %s FROM %s WHERE %s = %d", COLUMN_TASK_ID, COLUMN_DESCRIPTION, COLUMN_DUE, COLUMN_DONE, TABLE_NAME, COLUMN_TASK_ID, id);

        return null;
    }

    @Override
    public Task insertTask(Task t) {
        return null;
    }

    @Override
    public Task updateTask(Task t) {
        return null;
    }

    @Override
    public int deleteTask(Task t) {
        return 0;
    }
}