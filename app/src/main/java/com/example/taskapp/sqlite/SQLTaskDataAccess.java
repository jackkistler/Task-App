package com.example.taskapp.sqlite;//package com.example.taskapp2021.sqlite;//package com.example.taskapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

        Cursor c = database.rawQuery(query, null);
        if(c != null && c.getCount() > 0){
            c.moveToFirst();
            while(!c.isAfterLast()){
                long id = c.getLong(0);
                String description = c.getString(1);
                String due = c.getString(2);
                Boolean done = (c.getLong(3) == 1 ? true : false);

                Date dueDate = null;
                try {
                    dueDate = dateFormat.parse(due);
                } catch (ParseException e) {
//                    e.printStackTrace();
                    Log.d(TAG, "Unable to Parse Date" + e.getMessage());
                }

                Task t = new Task(id, description, dueDate, done);
                tasks.add(t);
                c.moveToNext();
            }
            c.close();
        }

        return tasks;
    }

    @Override
    public Task getTaskById(long id) {
        String query = String.format("SELECT %s, %s, %s, %s FROM %s WHERE %s = %d", COLUMN_TASK_ID, COLUMN_DESCRIPTION, COLUMN_DUE, COLUMN_DONE, TABLE_NAME, COLUMN_TASK_ID, id);

        Cursor c = database.rawQuery(query, null);
        c.moveToFirst();
        String description = c.getString(1);
        String due = c.getString(2);
        boolean done = c.getLong(3) == 1? true : false;

        Date dueDate = null;
        try {
            dueDate = dateFormat.parse(due);
        } catch (ParseException e) {
//            e.printStackTrace();
            Log.d(TAG, "Unable to Parse Date" + e.getMessage());
        }

        c.close();
        Task t = new Task(id, description, dueDate, done);

        return t;
    }

    @Override
    public Task insertTask(Task t) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, t.getDescription());
        values.put(COLUMN_DUE, dateFormat.format(t.getDue()));
        values.put(COLUMN_DONE, t.isDone() ? 1 : 0);

        long instertId = database.insert(TABLE_NAME, null, values);
        t.setId(instertId);
        return t;
    }

    @Override
    public Task updateTask(Task t) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, t.getDescription());
        values.put(COLUMN_DUE, dateFormat.format(t.getDue()));
        values.put(COLUMN_DONE, t.isDone() ? 1 : 0);
        int rowsUpdated = database.update(TABLE_NAME, values, COLUMN_TASK_ID + "=" + t.getId(), null);

        return t;
    }

    @Override
    public int deleteTask(Task t) {
        int rowsDeleted = database.delete(TABLE_NAME, COLUMN_TASK_ID + "=" + t.getId(), null);
        return 0;
    }
}