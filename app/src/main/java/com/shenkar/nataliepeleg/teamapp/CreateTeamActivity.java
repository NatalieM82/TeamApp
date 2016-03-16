package com.shenkar.nataliepeleg.teamapp;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CreateTeamActivity extends AppCompatActivity {

    ArrayList<String> emailList;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Create a team");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                addTeamMemberDiaglod();

            }
        });

        lvItems = (ListView) findViewById(R.id.listView);
        emailList = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, emailList);
        lvItems.setAdapter(itemsAdapter);
    }


    public boolean addTeamMemberDiaglod() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a team member");
        builder.setMessage("Enter email");
        final EditText inputField = new EditText(this);
        builder.setView(inputField);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("Member added", inputField.getText().toString());
                emailList.add(inputField.getText().toString());
                updateUI();
            }
        });

        builder.setNegativeButton("Cancel",null);

        builder.create().show();
        return true;
    }

    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.memberEmail);
        String email = taskTextView.getText().toString();

        emailList.remove(email);
        updateUI();
    }

    private void updateUI() {
//
//        listAdapter = new SimpleCursorAdapter(
//                this,
//                R.layout.task_view,
//                cursor,
//                new String[] { TaskContract.Columns.TASK},
//                new int[] { R.id.taskTextView},
//                0
//        );
//        this.setListAdapter(listAdapter);
        itemsAdapter.notifyDataSetChanged();
    }

}


