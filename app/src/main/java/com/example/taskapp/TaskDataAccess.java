package com.example.taskapp;

import com.example.taskapp.models.Task;
import java.util.ArrayList;
import java.util.Date;

public class TaskDataAccess {

    public static ArrayList<Task> allTasks = new ArrayList(){{
        add(new Task(1, "Mow the Lawn", new Date(123,8,22), false));
        add(new Task(2, "Take out Trash", new Date(123,8,22), false));
        add(new Task(3, "Pay Rent", new Date(123,8,22), true));
    }};

    public Task insertTask(Task t){
        t.setId(allTasks.size() + 1);
        allTasks.add(t);
        return t;
    }

    public ArrayList<Task> getAllTasks(){
        return allTasks;
    }

    public Task getTaskById(long id){
        for(Task t : allTasks){
            if(t.getId() == id){
                return t;
            }
        }
        return null;
    }

    public Task updateTask(Task t){
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
