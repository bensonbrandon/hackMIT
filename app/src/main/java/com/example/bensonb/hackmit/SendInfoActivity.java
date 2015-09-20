package com.example.bensonb.hackmit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseObject;
import com.parse.ParsePush;

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
        String locationString = location.getText().toString();
        EditText details = (EditText)findViewById(R.id.details);
        String detailsString = details.getText().toString();

        // create ParseObject request
        ParseObject request = new ParseObject("Request");
        request.put("issue", complaintString);
        request.put("location", locationString);
        request.put("details", detailsString);
        request.put("accepted", false);
        request.saveInBackground();

        // send push notification
        ParsePush push = new ParsePush();
        push.setChannel("EMT");
        push.setMessage("urgent request at " + request.get("location"));
        try {
            JSONObject data = new JSONObject().put("id", request.get("objectId"));
            push.setData(data);
            push.sendInBackground();
        } catch (Exception e) {
            Log.d("SendInfoActivity", "Failed to create request data for push notification.");
        }

        Intent intent = new Intent(this, WaitActivity.class);
        startActivity(intent);
    }
}
