package com.example.taskapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.taskapp.models.Task;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskDataAccess tda = new TaskDataAccess(this);
        ArrayList<Task> tasks = tda.getAllTasks();

        for(Task t : tasks){
            Log.d(TAG, t.toString());
        }

        Task newTask = new Task("Haircut", new Date(), false);
        try {
            newTask = tda.insertTask(newTask);
        } catch (Exception e) {
            //e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
        Log.d(TAG, "NewTask ID: " + newTask.getId());

        Task someTask = tda.getTaskById(1);
        Log.d(TAG, someTask.toString());

        someTask.setDescription("Do Homework");
        try {
            tda.updateTask(someTask);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        Log.d(TAG, someTask.toString());

        int numRows = tda.deleteTask(newTask);
        Log.d(TAG, "numrows: " + numRows);



    }
}