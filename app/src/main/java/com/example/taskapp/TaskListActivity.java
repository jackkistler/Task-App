package com.example.taskapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.taskapp.fileio.CSVTaskDataAccess;
import com.example.taskapp.models.Task;
import com.example.taskapp.sqlite.SQLTaskDataAccess;

import java.sql.Array;
import java.util.ArrayList;

public class TaskListActivity extends AppCompatActivity {

    public static final String TAG = "TaskListActivity";

    private ListView lsTasks;
    private Taskable da;
    private ArrayList<Task> allTasks;
    private Button btnAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        //Test code
        SQLTaskDataAccess sda = new SQLTaskDataAccess(this);

        btnAddTask = findViewById(R.id.btnAddTask);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TaskListActivity.this, TaskDetailsActivity.class);
                startActivity(i);
            }
        });

        lsTasks = findViewById(R.id.lsTasks);
//        da = new TaskDataAccess(this);
        da = new CSVTaskDataAccess(this);
        allTasks = da.getAllTasks();

        //if there are no tasks, navigate to details activity
        if(allTasks == null || allTasks.size() == 0){
            Intent i = new Intent(this, TaskDetailsActivity.class);
            startActivity(i);
        }

        ArrayAdapter<Task> adapter = new ArrayAdapter(this, R.layout.custom_task_list_item, R.id.lblDescription, allTasks){
            @Override
            public View getView(int position, View convertView, ViewGroup parentListView){
                View listItemView = super.getView(position, convertView, parentListView);
                TextView lblDescription = listItemView.findViewById(R.id.lblDescription);
                CheckBox chkDone = listItemView.findViewById(R.id.chkDone);

                Task currentTask = allTasks.get(position);
                lblDescription.setText(currentTask.getDescription());
                chkDone.setChecked(currentTask.isDone());

                chkDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentTask.setDone(chkDone.isChecked());
                    }
                });

                listItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Display Details for " + currentTask.getId());
                    }
                });

                return listItemView;
            }
        };



        lsTasks.setAdapter(adapter);

        lsTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.d(TAG, "selected index: " + i);
//                Log.d(TAG, "selected task: " + allTasks.get(i).toString());
//                Log.d(TAG, "selected ID: " + allTasks.get(i).getId());
                Task selectedTask = allTasks.get(i);
                Intent intent = new Intent(TaskListActivity.this, TaskDetailsActivity.class);
                intent.putExtra(TaskDetailsActivity.EXTRA_TASK_ID, selectedTask.getId());
                startActivity(intent);
            }



        });


    }
}