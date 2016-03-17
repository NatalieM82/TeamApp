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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

public class CreateTeamActivity extends AppCompatActivity {

    ArrayList<String> emailList;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private Button createTeamBtn;
    private EditText teamName;
    ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Create a team");
        setSupportActionBar(toolbar);

        currentUser = ParseUser.getCurrentUser();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                addTeamMemberDiaglod();

            }
        });

        createTeamBtn = (Button) findViewById(R.id.createTeam);
        teamName = (EditText) findViewById(R.id.teamNameEditText);

        createTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitTeam();
            }
        });

        lvItems = (ListView) findViewById(R.id.listView);
        emailList = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                R.layout.member_list_item, R.id.memberEmail, emailList);
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
        itemsAdapter.notifyDataSetChanged();
    }

    private void submitTeam() {
        String teamNameTxt = teamName.getText().toString();
        final String[] team_id = new String[1];
        final ParseObject teamObject = new ParseObject("Team");
        teamObject.put("TeamName", teamNameTxt);
        teamObject.put("managerId", currentUser.getObjectId());

        final String sessionToken = currentUser.getSessionToken();



        teamObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Saved successfully.
                    Log.d("Team saved", "User update saved!");
                    team_id[0] = teamObject.getObjectId();

                    for(int i=0; i<emailList.size() ; i++){
                        ParseUser user = new ParseUser();
                        user.setUsername(emailList.get(i));
                        user.setPassword("1234");
                        user.setEmail(emailList.get(i));
                        user.put("manager", false);
                        user.put("TeamId", teamObject.getObjectId());

                        user.signUpInBackground(new SignUpCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Show a simple Toast message upon successful registration
                                    Toast.makeText(getApplicationContext(),
                                            "Successfully created the list.",
                                            Toast.LENGTH_LONG).show();
                                    try {
                                        ParseUser.become(sessionToken);
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Team creation Error", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
                    }
                } else {
                    // The save failed.
                    Log.d("Team saved error", "User update error: " + e);
                }
            }
        });


    }

}


