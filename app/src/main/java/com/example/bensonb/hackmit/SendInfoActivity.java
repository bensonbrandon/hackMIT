package com.example.bensonb.hackmit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.parse.ParseObject;

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
        request.saveInBackground();

        // send push notification

        Intent intent = new Intent(this, WaitActivity.class);
        startActivity(intent);
    }
    public void toMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
