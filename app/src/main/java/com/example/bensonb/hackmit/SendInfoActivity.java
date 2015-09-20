package com.example.bensonb.hackmit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
/**
 * Created by bensonb on 9/19/2015.
 */
public class SendInfoActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Context ctx = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_sendinfo);
        setUpMapIfNeeded();
    }

    public void toWait(View view) {

        boolean success = true;
        // get info from text boxes
        EditText complaint = (EditText)findViewById(R.id.complaint);
        String complaintString = complaint.getText().toString();
        EditText location = (EditText)findViewById(R.id.location);
        final String locationString = location.getText().toString();
        EditText details = (EditText)findViewById(R.id.details);
        String detailsString = details.getText().toString();

        // create ParseObject request
        final ParseObject request = new ParseObject("Request");
        request.put("issue", complaintString);
        request.put("location", locationString);
        request.put("details", detailsString);
        request.put("accepted", false);
        request.put("valid", true);
        request.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {

                if (e == null) {
                    // Saved successfully.
                    // send push notification
                    Log.d("SendInfoActivity", "About to send push notification");
                    ParsePush push = new ParsePush();
                    String channel = getIntent().getStringExtra("CHANNEL");
                    push.setChannel(channel);
                    try {
                        JSONObject data = new JSONObject();
                        data.put("id", request.getObjectId());
                        data.put("alert", "urgent request at " + locationString);
                        Log.d("PushNotification", data.toString());
                        push.setData(data);
                        push.sendInBackground();

                        Intent intent = new Intent(ctx, WaitActivity.class);
                        intent.putExtra("requestId", request.getObjectId());
                        startActivity(intent);
                        finish();
                    } catch (Exception exc) {
                        Log.d("SendInfoActivity", "Failed to create request data for push notification.");
                    }
                } else {
                    // The save failed.
                    Log.d("SendInfoActivity", "Error updating user data: " + e);
                    request.put("valid", false);
                }

            }
        });


    }
    public void toMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(42.35848,-71.09651)).title("Patient Location"));
        LatLng latLng = new LatLng(42.35848,-71.09651);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);

    }

}
