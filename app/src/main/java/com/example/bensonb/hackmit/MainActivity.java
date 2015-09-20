package com.example.bensonb.hackmit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * Created by bensonb on 9/19/2015.
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ParseUser.getCurrentUser() != null) {
            Button loginButton = (Button)findViewById(R.id.login_button);
            loginButton.setText("Change User");
        }
    }
    public void toLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void toSendEMTInfo(View view) {
        Intent intent = new Intent(this, SendInfoActivity.class);
        intent.putExtra("CHANNEL", "EMT");
        startActivity(intent);
    }

    public void toSendMLinkInfo(View view) {
        Intent intent = new Intent(this, SendInfoActivity.class);
        intent.putExtra("CHANNEL", "Medlink");
        startActivity(intent);
    }

    public void toSendCPRInfo(View view) {
        Intent intent = new Intent(this, SendInfoActivity.class);
        intent.putExtra("CHANNEL", "CPR");
        startActivity(intent);
    }
}