package com.shenkar.nataliepeleg.teamapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

public class MainActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Determine whether the current user is an anonymous user
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // If user is anonymous, send the user to LoginSignupActivity.class
            Intent intent = new Intent(MainActivity.this,
                    LoginSignupActivity.class);
            startActivity(intent);
            finish();
        } else {
            // If current user is NOT anonymous user
            // Get current user data from Parse.com

            ParseUser currentUser = ParseUser.getCurrentUser();
            Boolean isManager = currentUser.getBoolean("manager");
            if (currentUser != null) {
                if (isManager == true) {
                    Intent intent = new Intent(
                            MainActivity.this,
                            Welcome.class);
                    startActivity(intent);
                } else {
                    String team_id = currentUser.getString("TeamId");
                    Intent intent = new Intent(
                            MainActivity.this,
                            TasksActivity2.class);
                    intent.putExtra("TEAM_ID", team_id);
                    startActivity(intent);
                }
            } else {
                // Send user to LoginSignupActivity.class
                Intent intent = new Intent(MainActivity.this,
                        LoginSignupActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }
}