package com.example.bensonb.hackmit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONObject;

/**
 * Created by bensonb on 9/19/2015.
 */
public class AcceptRejectActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    String requestId = null;
    Context ctx = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_acceptreject);
        try {
            JSONObject json = new JSONObject(getIntent().getStringExtra("com.parse.Data"));
            requestId = json.getString("id");
            Log.d("AcceptRejectActivity", "id is " + requestId);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
            // Retrieve the object by id
            query.getInBackground(requestId, new GetCallback<ParseObject>() {
                public void done(ParseObject request, ParseException e) {
                    if (e == null) {
                        if (!request.getBoolean("accepted") && request.getBoolean("valid")) {
                            updateFields(request);
                        }
                    }
                }
            });
        } catch (Exception e) {
            Log.d("AcceptRejectActivity", "Failed to get intent data from push notification.");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        setUpMapIfNeeded();
    }

    public void updateFields(ParseObject request) {
        String location = request.getString("location");
        String details = request.getString("details");
        String issue = request.getString("issue");

        TextView locationBox = (TextView)findViewById(R.id.patientlocation);
        locationBox.setText(location);
        TextView detailBox = (TextView)findViewById(R.id.additionalinfo);
        detailBox.setText(details);
        TextView issueBox = (TextView)findViewById(R.id.complaint);
        issueBox.setText(issue);
    }
    public void toMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void acceptRequest(final View view) {
        if (requestId == null) {
            Log.d("AcceptReject", "no request to accept");
            toMain(view);
            finish();
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
        // Retrieve the object by id
        query.getInBackground(requestId, new GetCallback<ParseObject>() {
            public void done(ParseObject request, ParseException e) {
                if (e == null) {
                    if (request.getBoolean("accepted") || !request.getBoolean("valid")) {
                        Log.d("AcceptReject", "request already accepted or cancelled");
                    } else {
                        request.put("accepted", true);
                        EditText etaBox = (EditText) findViewById(R.id.minutes);
                        final Integer eta = Integer.parseInt(etaBox.getText().toString());
                        if (eta > 0) {
                            request.put("eta", eta);
                        }
                        request.saveInBackground();
                        // set acceptor to current user username???
                        Intent intent = new Intent(ctx, Accept.class);
                        intent.putExtra("requestId", request.getObjectId());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    public void rejectRequest(View view) {
        toMain(view);
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
