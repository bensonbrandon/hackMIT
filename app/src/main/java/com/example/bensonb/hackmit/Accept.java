package com.example.bensonb.hackmit;

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
import com.parse.ParseQuery;

import java.util.Timer;
import java.util.TimerTask;

public class Accept extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);

        String requestId = getIntent().getStringExtra("requestId");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
        // Retrieve the object by id
        query.getInBackground(requestId, new GetCallback<ParseObject>() {
            public void done(ParseObject request, ParseException e) {
                final ParseObject requestObject = request;
                if (e == null) {
                    EditText etaBox = (EditText) findViewById(R.id.inputnumber);
                    Integer eta = request.getInt("eta");
                    etaBox.setText(eta);
                }
            }
        });
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
                final ParseObject requestObject = request;
                if (e == null) {
                    request.put("eta", eta);
                    request.saveInBackground();
                }
            }
        });
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
