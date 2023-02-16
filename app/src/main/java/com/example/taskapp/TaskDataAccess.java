package com.example.taskapp;

import android.content.Context;

import com.example.taskapp.models.Task;
import java.util.ArrayList;
import java.util.Date;

public class TaskDataAccess {

    private Context context;

    public TaskDataAccess(Context c){
        this.context = c;
    }

    private static ArrayList<Task> allTasks = new ArrayList(){{
        add(new Task(1, "Mow the Lawn", new Date(123,8,22), false));
        add(new Task(2, "Take out Trash", new Date(123,8,22), false));
        add(new Task(3, "Pay Rent", new Date(123,8,22), true));
    }};

    public Task insertTask(Task t){
        t.setId(allTasks.size());
        allTasks.add(t);
        return t;
    }

    public ArrayList<Task> getAllTasks(){
        return allTasks;
    }

    public Task getTaskById(long id){
        for(Task t : allTasks){
            if(t.getId() == id){
                return new Task(t.getId(), t.getDescription(), t.getDue(), t.isDone());
            }
        }
        return null;
    }

    public Task updateTask(Task t){
        Task original = getTaskById(t.getId());
        original.setDescription((t.getDescription()));
        original.setDone(t.isDone());
        original.setDue(t.getDue());
        return t;
    }

    public int deleteTask(Task t){
        if (allTasks.contains(t)){
            allTasks.remove(t);
            return 1;
        }
        return 0;
    }


}
