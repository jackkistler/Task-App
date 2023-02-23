package com.example.taskapp;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import com.example.taskapp.models.Task;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;

public class TaskDataAccess {

    private Context context;
    public static final String TAG = "TaskDataAccess";

    public TaskDataAccess(Context c){
        this.context = c;
    }

    private static ArrayList<Task> allTasks = new ArrayList(){{
        add(new Task(1, "Mow the Lawn", new Date(123,8,22), false));
        add(new Task(2, "Take out Trash", new Date(123,8,22), false));
        add(new Task(3, "Pay Rent", new Date(123,8,22), true));
    }};

    public Task insertTask(Task t) throws Exception {

        if(t.isValid()){
            allTasks.add(t);
            t.setId(getMaxId() + 1);
            return t;
        }else{
            throw new Exception("INSERT FAILED INVALID TASK");
        }

    }

    private long getMaxId(){
        long maxId = 0;
        for(Task t : allTasks){
            long tId = t.getId();
            maxId = tId > maxId ? tId : maxId;
        }
        return maxId;
    }

    public ArrayList<Task> getAllTasks(){
       // return allTasks;
        ArrayList<Task> copyList = new ArrayList();
        for (Task t : allTasks){
            copyList.add(new Task(t.getId(), t.getDescription(), t.getDue(), t.isDone()));
        }
        return copyList;

    }

    public Task getTaskById(long id){
        for(Task t : allTasks){
            if(t.getId() == id){
                return new Task(t.getId(), t.getDescription(), t.getDue(), t.isDone());
                //return t;
            }
        }
        return null;
    }

    public Task updateTask(Task t) throws Exception {

        if(t.isValid()){
            for(Task task : allTasks){
                if(task.getId() == t.getId()){
                    task.setDescription(t.getDescription());
                    task.setDue(t.getDue());
                    task.setDone(t.isDone());
                    break;
                }
            }
            return t;
        }else{
            throw new Exception("UPDATE FAILED INVALID TASK");
        }

    }

    public int deleteTask(Task t){
//        if (allTasks.contains(t)){
//            allTasks.remove(t);
//            return 1;
//        }
//        return 0;
        for(Task currentTask : allTasks){
            if(currentTask.getId() == t.getId()){
                allTasks.remove(currentTask);
                return 1;
            }
        }
        return 0;
    }




}
