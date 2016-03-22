package com.shenkar.nataliepeleg.teamapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by NatalieMenahem on 17/03/2016.
 */
public class TasksAdapter extends ArrayAdapter<Task> {
    ArrayList<Task> tasks;


    public TasksAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
        this.tasks = tasks;
    }

    @Override
    public int getCount(){
        return tasks!=null ? tasks.size() : 0;
    }

    public ArrayList<Task> getItems() {
        return tasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tasks_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.taskText);
        // Populate the data into the template view using the data object
        tvName.setText(task.text);


//        Button btnNxt = (Button) findViewById(R.id.btnNext);
//        btnNxt.setTag(position);
//        btnNxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                int position = (Integer) arg0.getTag();
//            }
//        });
        // Return the completed view to render on screen
        return convertView;
    }
}
