package com.example.bensonb.hackmit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bensonb on 9/19/2015.
 */
public class WaitActivity extends Activity {

    String requestId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        requestId = getIntent().getStringExtra("requestId");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
        // Retrieve the object by id
        query.getInBackground(requestId, new GetCallback<ParseObject>() {
            public void done(ParseObject request, ParseException e) {
                final ParseObject requestObject = request;
                if (e == null) {
                    try {
                        Timer timer = new Timer();
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    requestObject.fetch();
                                    if (requestObject.getBoolean("accepted")) {
                                        updateRequestStatus(requestObject.getInt("eta"));
                                    } else {
                                        badRequestStatus();
                                    }
                                } catch (ParseException pe) {
                                    Log.d("WaitActivity", "failed to fetch latest data");
                                }
                            }
                        };
                        timer.schedule(timerTask,0, 1000);
                    } catch (IllegalStateException ise){
                        Log.d("WaitActivity", "unknown failure while updating page");
                    }
                }
            }
        });

    }

    public void toMain(View view) {
        if (requestId == null) {
            Log.d("WaitActivity", "no request to invalidate");
            toMain(view);
            finish();
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
        // Retrieve the object by id
        query.getInBackground(requestId, new GetCallback<ParseObject>() {
            public void done(ParseObject request, ParseException e) {
                if (e == null) {
                    request.put("valid", false);
                }
            }
        });
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    void badRequestStatus()
    {
        final TextView statusBox =(TextView)findViewById(R.id.request_status);
        statusBox.post(new Runnable() {
            @Override
            public void run() {
                String status = statusBox.getText().toString();
                if (status.startsWith("Your request")) {
                    statusBox.setText("Your request's acceptor had to cancel. Searching for new volunteer...");
                }
            }
        });
    }

    void updateRequestStatus(Integer minutes)
    {
        final Integer eta = minutes;
        final TextView statusBox =(TextView)findViewById(R.id.request_status);

        statusBox.post(new Runnable() {
            @Override
            public void run() {
                statusBox.setText("Your request has been accepted! ETA: " + eta.toString() +
                        " minutes");
            }
        });
    }
}


