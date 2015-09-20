package com.example.bensonb.hackmit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;

/**
 * Created by bensonb on 9/19/2015.
 */
public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void toMain(View view) {
        EditText username = (EditText)findViewById(R.id.username);
        String userString = username.getText().toString();
        EditText password = (EditText)findViewById(R.id.password);
        Log.d("hello", "no");
        ParseUser.logInInBackground(userString, password.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    // deal with subscription to channels and installation stuff
                    ParseInstallation.getCurrentInstallation().saveInBackground();
                    if (user.getBoolean("isEMT")) {
                        ParsePush.subscribeInBackground("EMT");
                    }
                    if (user.getBoolean("isMedlink")) {
                        ParsePush.subscribeInBackground("Medlink");
                    }
                    if (user.getBoolean("isCPR")) {
                        ParsePush.subscribeInBackground("CPR");
                    }
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    Log.d("Login", "User authentication failed.");
                }
            }
        });
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
