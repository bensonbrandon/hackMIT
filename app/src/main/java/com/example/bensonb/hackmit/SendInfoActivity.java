package com.example.bensonb.hackmit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        Intent intent = new Intent(this, WaitActivity.class);
        startActivity(intent);
    }
}
