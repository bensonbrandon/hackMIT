package com.example.bensonb.hackmit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.parse.Parse;
import com.parse.ParseObject;

public class Main0Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "7uSbWIp5mdcf1dgUnYiEkgyL1rL6BeBz5bYvkoRA", "AAB4OWTqlgWy38bN1mZXWIFyRxbkdphbwXFc3uGM");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
