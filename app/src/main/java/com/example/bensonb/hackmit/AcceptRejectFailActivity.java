package com.example.bensonb.hackmit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by bensonb on 9/20/2015.
 */
public class AcceptRejectFailActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptreject_fail);
    }
    public void toExit(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
