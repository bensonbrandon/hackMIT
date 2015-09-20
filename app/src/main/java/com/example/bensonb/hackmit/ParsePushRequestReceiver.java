package com.example.bensonb.hackmit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONObject;

/**
 * Created by Meghana Bhat on 9/20/2015.
 */
public class ParsePushRequestReceiver extends ParsePushBroadcastReceiver {

    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        return com.example.bensonb.hackmit.AcceptRejectActivity.class;
    }

}
