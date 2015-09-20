package com.example.bensonb.hackmit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import org.json.JSONObject;

/**
 * Created by bensonb on 9/19/2015.
 */
public class SendInfoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendinfo);
    }

    public void toWait(View view) {
        // get info from text boxes
        EditText complaint = (EditText)findViewById(R.id.complaint);
        String complaintString = complaint.getText().toString();
        EditText location = (EditText)findViewById(R.id.location);
        final String locationString = location.getText().toString();
        EditText details = (EditText)findViewById(R.id.details);
        String detailsString = details.getText().toString();

        // create ParseObject request
        final ParseObject request = new ParseObject("Request");
        request.put("issue", complaintString);
        request.put("location", locationString);
        request.put("details", detailsString);
        request.put("accepted", false);
        request.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Saved successfully.
                    // send push notification
                    Log.d("SendInfoActivity", "About to send push notification");
                    ParsePush push = new ParsePush();
                    String channel = getIntent().getStringExtra("CHANNEL");
                    push.setChannel(channel);
                    try {
                        JSONObject data = new JSONObject();
                        data.put("id", request.getObjectId());
                        data.put("alert", "urgent request at " + locationString);
                        Log.d("PushNotification", data.toString());
                        push.setData(data);
                        push.sendInBackground();
                    } catch (Exception exc) {
                        Log.d("SendInfoActivity", "Failed to create request data for push notification.");
                    }
                } else {
                    // The save failed.
                    Log.d("SendInfoActivity", "Error updating user data: " + e);
                }
            }
        });

        Intent intent = new Intent(this, WaitActivity.class);
        startActivity(intent);
    }
    public void toMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
