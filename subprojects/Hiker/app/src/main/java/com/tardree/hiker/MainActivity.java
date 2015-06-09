package com.tardree.hiker;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private TextView latitude;
    private TextView longitude;
    private TextView providerInformation;
    private String FILENAME = "coordinates.txt";
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private File file;
    private FileOutputStream fOut;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS");

    public static final String TAG = MainActivity.class.getSimpleName();

    private void showLocation(Location location)
    {
        if (location == null)
        {
            Toast.makeText(MainActivity.this, "Location is null",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            latitude = (TextView) findViewById(R.id.latitude);
            longitude = (TextView) findViewById(R.id.longitude);
            providerInformation = (TextView) findViewById(R.id.providerInformation);
            latitude.setText("Latitude: " + String.valueOf(location.getLatitude()));
            longitude.setText("Longitude: " + String.valueOf(location.getLongitude()));
            providerInformation.setText("Provider: " + String.valueOf(location.getProvider()));
            Date now = Calendar.getInstance().getTime();
            String nowStr = dateFormat.format(now);
            String content = nowStr + ", " + String.valueOf(location.getLatitude()) + ", " + String.valueOf(location.getLongitude());
            try {
                fOut.write(content.getBytes());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this, location.getProvider() + " " + ": Wrote : " + content + " : to : " + file.getAbsolutePath(),
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void locationSetup()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        int GPS_INTERVAL = 10 * 1000; // 10 seconds
        int GPS_FASTEST_INTERVAL = 1 * 1000; // 1 second

        // Create the LocationRequest object
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(GPS_INTERVAL)        // 10 seconds, in milliseconds
                .setFastestInterval(GPS_FASTEST_INTERVAL); // 1 second, in milliseconds

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/download");
        dir.mkdirs();
        file = new File(dir, FILENAME);

        try {
            fOut = new FileOutputStream(file);
//            fOut = openFileOutput(file, MODE_WORLD_READABLE);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        showLocation(location);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "Entered onCreate");
        setContentView(R.layout.activity_main);
        locationSetup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "Entered onResume");
        setContentView(R.layout.activity_main);
        locationSetup();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("MainActivity", "Entered onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MainActivity", "Entered onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case R.id.map_display:
                Log.d("onOptionsItemSelected", "map_display - about to display map");
                MapsActivity.launch(this);
                break;
            case R.id.action_settings :
                Log.d("onOptionsItemSelected", "action_settings - returning true");
                return true;
        }

        Log.d("onOptionsItemSelected", "returning super.onOptionsItemSelected");
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
        else {
            showLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, MapsActivity.CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        showLocation(location);
    }


}
