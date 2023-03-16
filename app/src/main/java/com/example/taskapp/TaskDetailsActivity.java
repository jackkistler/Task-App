package com.example.taskapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskapp.fileio.CSVTaskDataAccess;
import com.example.taskapp.models.Task;
import com.example.taskapp.sqlite.SQLTaskDataAccess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskDetailsActivity extends AppCompatActivity {

    public static final String TAG = "TaskDetailsActivity";
    public static final String EXTRA_TASK_ID = "taskId";
    Taskable da;
    Task task;

    EditText txtDescription;
    EditText txtDueDate;
    CheckBox chkDone;
    Button btnSave;
    Button btnDelete;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/d/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);



        txtDescription = findViewById(R.id.txtDescription);
        txtDueDate = findViewById(R.id.txtDueDate);
        chkDone = findViewById(R.id.chkDone);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(save()){
                    Intent i = new Intent(TaskDetailsActivity.this, TaskListActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(TaskDetailsActivity.this, "Unable to save task", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "delete task " + task.getId());
//                da.deleteTask(task);
//                Intent i = new Intent(TaskDetailsActivity.this, TaskListActivity.class);
//                startActivity(i);

                showDeleteDialog();
            }
        });

        //da = new TaskDataAccess(this);
//        da = new CSVTaskDataAccess(this);
        da = new SQLTaskDataAccess(this);
        Intent i = getIntent();
        long id = i.getLongExtra(EXTRA_TASK_ID, 0);

        if(id > 0){
            task = da.getTaskById(id);
            Log.d(TAG, task.toString());

            putDataIntoUI();
            btnDelete.setVisibility(View.VISIBLE);
        }

    }

    private void putDataIntoUI(){
        if(task != null){
            txtDescription.setText(task.getDescription());

//            String dateStr = null;
//            SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
//            dateStr = sdf.format(task.getDue());
//            txtDueDate.setText(dateStr);

            txtDueDate.setText(sdf.format(task.getDue()));

            chkDone.setChecked(task.isDone());
        }
    }

    private boolean validate(){
        boolean isValid = true;

        if (txtDescription.getText().toString().isEmpty()){
            isValid = false;
            txtDescription.setError("You must enter a description");
        }

        Date dueDate = null;

        //SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
//        if(txtDueDate.getText().toString().isEmpty()){
//            isValid = false;
//            txtDueDate.setError("You must enter a date");
//        }else{
//            try {
//                dueDate = sdf.parse(txtDueDate.getText().toString());
//            } catch (ParseException e) {
//                e.printStackTrace();
//                isValid = false;
//                txtDueDate.setError("You must enter a valid date");
//            }
//        }

        String regex = "^(1[0-2]|0[1-9])/(3[01]"
                + "|[12][0-9]|0[1-9])/[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(txtDueDate.getText().toString());
        if(!matcher.matches()){
            isValid = false;
            txtDueDate.setError("The date entered is not valid");
        }


        return isValid;
    }

    private boolean save(){
        if(validate()){
            getDataFromUI();
            if(task.getId() > 0){
                Log.d(TAG, "Update");
                try {
                    da.updateTask(task);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
                //Log.d(TAG, task.toString());
            }else{
                Log.d(TAG, "Insert");
                try {
                    da.insertTask(task);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }



            return true;
        }
        return false;
    }

    private void getDataFromUI(){
        String desc = txtDescription.getText().toString();
        boolean done = chkDone.isChecked();

        String dueDateStr = txtDueDate.getText().toString();
        Date date = null;
        //SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        try {
            date = sdf.parse(dueDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "Unable to parse date from string");
        }

        if(task != null){
            task.setDescription(desc);
            task.setDue(date);
            task.setDone(done);
        }else{
            task = new Task(desc, date, done);
        }
    }

    private void showDeleteDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("WHAT ARE YOU DOING!?");
        alert.setMessage("Are you sure you want ot delete this task?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //actual deletion
                da.deleteTask(task);
                startActivity( new Intent(TaskDetailsActivity.this, TaskListActivity.class));
                //
            }
        });
        alert.setNegativeButton("NO!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }


}