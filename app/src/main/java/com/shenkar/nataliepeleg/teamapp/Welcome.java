package com.shenkar.nataliepeleg.teamapp;

/**
 * Created by NatalieMenahem on 07/03/2016.
 */

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Welcome extends AppCompatActivity {
    // Declare Variable
    Button logout;
    Button createTeam;
    Button showTeams;
    boolean isManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.welcome);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Welcome");
//        setSupportActionBar(toolbar);

        // Retrieve current user from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();

        // Convert currentUser into String
        String struser = currentUser.getUsername().toString();

        // Locate TextView in welcome.xml
        TextView txtuser = (TextView) findViewById(R.id.txtuser);

        // Set the currentUser String into TextView
        txtuser.setText("You are logged in as " + struser);

        isManager = currentUser.getBoolean("manager");


        // Locate Button in welcome.xml
        logout = (Button) findViewById(R.id.logout);

        createTeam = (Button) findViewById(R.id.createTeamBtn);
        showTeams = (Button) findViewById(R.id.showTeamsBtn);

        // Logout Button Click Listener
        logout.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Logout current user
                ParseUser.logOut();
                finish();
            }
        });

        createTeam.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(
                        Welcome.this,
                        CreateTeamActivity.class);
                startActivity(intent);
            }
        });

        showTeams.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(
                        Welcome.this,
                        ShowTeamsActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
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
            Intent intent = new Intent(Welcome.this,
                    SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_about) {
            Intent intent = new Intent(Welcome.this,
                    AboutActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(Welcome.this,
                    LoginSignupActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
