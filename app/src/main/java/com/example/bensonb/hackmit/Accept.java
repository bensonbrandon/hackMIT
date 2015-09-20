package com.example.bensonb.hackmit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Accept extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accept, menu);
        return true;
    }

    public void updateETA(View view) {
        EditText etaBox = (EditText) findViewById(R.id.inputnumber);
        final Integer eta = Integer.parseInt(etaBox.getText().toString());
        if (eta <= 0) {
            return;
        }

        String requestId = getIntent().getStringExtra("requestId");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
        // Retrieve the object by id
        query.getInBackground(requestId, new GetCallback<ParseObject>() {
            public void done(ParseObject request, ParseException e) {
                if (e == null) {
                    request.put("eta", eta);
                    request.saveInBackground();
                }
            }
        });
    }

    public void toMain(View view) {
        String requestId = getIntent().getStringExtra("requestId");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
        // Retrieve the object by id
        query.getInBackground(requestId, new GetCallback<ParseObject>() {
            public void done(ParseObject request, ParseException e) {
                if (e == null) {
                    request.put("accepted", false);
                    request.saveInBackground();
                    resendPushNotification(request);
                }
            }
        });
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void resendPushNotification(ParseObject request) {
        Log.d("SendInfoActivity", "About to send push notification");
        String channel = request.getString("channel");
        ParsePush push = new ParsePush();
        push.setChannel(channel);
        try {
            JSONObject data = new JSONObject();
            data.put("id", request.getObjectId());
            String alert = "urgent request at " + request.getString("location");
            if (channel == "Medlink") {
                alert = "non-" + alert;
            }
            data.put("alert", alert);
            Log.d("PushNotification", data.toString());
            push.setData(data);
            push.sendInBackground();
        } catch (Exception e) {
            Log.d("Accept", "failed to send data with new push notification");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
