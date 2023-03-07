package com.example.taskapp.fileio;

import android.content.Context;
import android.util.Log;

import com.example.taskapp.Taskable;
import com.example.taskapp.models.Task;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CSVTaskDataAccess implements Taskable {

    public static final String TAG = "CSVTaskDataAccess";
    public static final String DATA_FILE = "tasks.csv";

    private ArrayList<Task> allTasks;
    private Context context;

    public CSVTaskDataAccess(Context c){
        this.context = c;
        this.allTasks = new ArrayList();

        loadTasks();


        //TEST CODE
//        this.allTasks.add(new Task(1, "do something", new Date(), false));
//        this.allTasks.add(new Task(2, "do something else", new Date(), false));
//        saveTasks();
//
//        loadTasks();
//        Log.d(TAG, allTasks.toString());
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        loadTasks();
        return allTasks;
    }

    @Override
    public Task getTaskById(long id) {
        loadTasks();
        for(Task t : allTasks){
            if(t.getId() == id){
                return t;
            }
        }

        return null;
    }

    @Override
    public Task insertTask(Task t) throws Exception {
        if(t.isValid()){
            long newId = getMaxId() + 1;
            t.setId(newId);
            allTasks.add(t);
            saveTasks();
        }else{
            throw new Exception("Uh oh! That is an Invalid task");
        }

        return t;

    }

    @Override
    public Task updateTask(Task t) throws Exception {
        if(t.isValid()){
            Task taskToUpdate = getTaskById(t.getId());
            taskToUpdate.setDescription(t.getDescription());
            taskToUpdate.setDone(t.isDone());
            taskToUpdate.setDue(t.getDue());
            saveTasks();
        }else{
            throw new Exception("Bummer! That is an Invalid task");
        }

        return t;
    }

    @Override
    public int deleteTask(Task t) {
        Task taskToRemove = getTaskById(t.getId());
        if(taskToRemove != null){
            allTasks.remove(allTasks.indexOf(taskToRemove));
            saveTasks();
            return 1;
        }else{
            return 0;
        }

    }

    private String convertTaskToCSV(Task t){

        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");

        String retVal = t.getId() + ","
                + t.getDescription() + ","
                + sdf.format(t.getDue()) + ","
                + t.isDone();
        return retVal;
    }

    private Task convertCSVToTask(String csv){
        String[] data = csv.split(",");
        long id = Long.parseLong(data[0]);
        String desc = data[1];
        Date due = new Date(data[2]);
        boolean done = Boolean.parseBoolean(data[3]);

        return new Task(id, desc, due, done);
    }

    private void loadTasks(){
        ArrayList<Task> dataList = new ArrayList();
        String dataString = FileHelper.readFromFile(DATA_FILE, context);
        if(dataString == null){
            Log.d(TAG, "NO DATA FILE");
            return;
        }

        String[] lines = dataString.trim().split("\n");

        for(String line : lines){
            Task t = convertCSVToTask(line);
            if(t != null){
                dataList.add(t);
            }
        }

        this.allTasks = dataList;
    }

    private void saveTasks(){
        String dataString = "";
        StringBuilder sb = new StringBuilder();

        for(Task t : allTasks){
            String csv = convertTaskToCSV(t);
            sb.append(csv);
            sb.append("\n");
        }

        dataString = sb.toString();
        if(FileHelper.writeToFile(DATA_FILE, dataString, context)){
            Log.d(TAG, "Successfully saved data!");
        }else{
            Log.d(TAG, "oh no! you failed to save data!");
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
}
