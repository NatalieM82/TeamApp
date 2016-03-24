package com.shenkar.nataliepeleg.teamapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
    private String current_team_id;

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

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        current_team_id = intent.getStringExtra("TEAM_ID");

        if(current_team_id != null){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
            query.whereEqualTo("objectId", current_team_id);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        Log.d("Team:", objects.toString());
                        teamName.setText(objects.get(0).getString("TeamName"));
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Team retrieving failed", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });

            ParseQuery<ParseUser> query2 = ParseUser.getQuery();
            query2.whereEqualTo("TeamId", current_team_id);
            query2.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        Log.d("Team members", objects.toString());
                        for (int i = 0; i < objects.size(); i++) {
                            emailList.add(objects.get(i).getUsername());
                        }
                        updateUI();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Team members retrieving failed", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });



        }
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

//    public void onDoneButtonClick(View view) {
//        View v = (View) view.getParent();
//        TextView taskTextView = (TextView) v.findViewById(R.id.memberEmail);
//        String email = taskTextView.getText().toString();
//
//        emailList.remove(email);
//        updateUI();
//    }


//       @Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_tasks_activity2, menu);
    return true;
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(CreateTeamActivity.this,
                    SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_about) {
            Intent intent = new Intent(CreateTeamActivity.this,
                    AboutActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(CreateTeamActivity.this,
                    LoginSignupActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        itemsAdapter.notifyDataSetChanged();
    }

    private void submitTeam() {
        String teamNameTxt = teamName.getText().toString();
        if (teamNameTxt.equals("")) return;
        final String[] team_id = new String[1];
        final ParseObject teamObject = new ParseObject("Team");
        teamObject.put("TeamName", teamNameTxt);
        teamObject.put("managerId", currentUser.getObjectId());

        final String sessionToken = currentUser.getSessionToken();

        if(current_team_id == null)
        {
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
        else {
            ParseObject point = ParseObject.createWithoutData("Team", current_team_id);

            // Set a new value on quantity
            point.put("TeamName", teamNameTxt);

            // Save
            point.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Saved successfully.
                        Toast.makeText(getApplicationContext(),
                                "Team updated successfully", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        // The save failed.
                        Toast.makeText(getApplicationContext(),
                                "Error saving, try again", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });


            for(int i=0; i<emailList.size() ; i++){
                ParseUser user = new ParseUser();
                user.setUsername(emailList.get(i));
                user.setPassword("1234");
                user.setEmail(emailList.get(i));
                user.put("manager", false);
                user.put("TeamId", current_team_id);

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Show a simple Toast message upon successful registration
                            Toast.makeText(getApplicationContext(),
                                    "Successfully created user.",
                                    Toast.LENGTH_LONG).show();
                            try {
                                ParseUser.become(sessionToken);
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "User creation Error: " + e, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
            }
        }

        Log.i("Send email", "");

        String[] TO = emailList.toArray(new String[0]);
        String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Invitation to Join TeamApp team");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi\n" +
                "You have been invited to be a team member in an TeamApp Team created by me.\n" +
                "Use this link to download and install the App from Google Play.\n" +
                "https://play.google.com/store/apps/details?id=com.shenkar.nataliepeleg.teamapp\n\n" +
                "Username will be your email address and password: 1234");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CreateTeamActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }



    }

}


