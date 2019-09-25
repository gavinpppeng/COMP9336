package com.example.lab6;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private TextView GPS_status;
    private TextView locationInfo;
    private Button btnGetGPSStatus;
    private Button btnGetLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        btnGetGPSStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Although GPS is most accurate, it only works outdoors, it quickly consumes battery power, and doesn't return the location as quickly as users want.
                // Android's Network Location Provider determines user location using cell tower and Wi-Fi signals,
                // providing location information in a way that works indoors and outdoors, responds faster, and uses less battery power.
                // We can use both GPS and the Network Location Provider, or just one.
                boolean gps_status = locationManager.isProviderEnabled(GPS_PROVIDER);
                boolean network_status = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (gps_status || network_status) {
                    GPS_status.setText("GPS is active.");
                    btnGetLocation.setVisibility(View.VISIBLE);
                } else {
                    GPS_status.setText("GPS is not active.");
                    showAlertDialog();
                }
            }
        });

        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationInfo.setVisibility(View.VISIBLE);
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }

                //if GPS provider is not null, getting the last information. But it maybe out-of-date
                Location location_gps = locationManager.getLastKnownLocation(GPS_PROVIDER);
                if (location_gps != null){
                    String gps_LastTime = GetLocation(location_gps);
                    locationInfo.setText(gps_LastTime);
                }

                else{
                    Location network_gps = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
                    if(network_gps != null) {
                        String network_LastTime = GetLocation(network_gps);
                        locationInfo.setText(network_LastTime);
                    }
                }
            }
        });

    }

    //Used for receiving notifications from the LocationManager when the location has changed.
    // These methods are called if the LocationListener has been registered with the location manager service
    // using the LocationManager#requestLocationUpdates(String, long, float, LocationListener) method.
    @Override
    public void onLocationChanged(Location location) {
        String gpslocation = GetLocation(location);
        locationInfo.setText(gpslocation);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    private void init() {
        //getting the location service of mobile phone
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        GPS_status = (TextView) findViewById(R.id.GPS_status);
        locationInfo = (TextView) findViewById(R.id.locationInfo);
        btnGetGPSStatus = (Button) findViewById(R.id.btnGpsStatus);
        btnGetLocation = (Button) findViewById(R.id.btnLocation);

        //A class indicating the application criteria for selecting a location provider.
        // Providers may be ordered according to accuracy, power usage, ability to report altitude, speed, bearing, and monetary cost.
        //Criteria criteria = new Criteria();
        //criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);
        //criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
       // String bestprovider = locationManager.getBestProvider(criteria, true);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        //Register for location updates using the named provider, and a listener.
        //minTime	long: minimum time interval between location updates, in milliseconds
        //minDistance	float: minimum distance between location updates, in meters
        //listener	LocationListener: a LocationListener whose LocationListener#onLocationChanged method will be called for each location update
        // This value must never be null.
        locationManager.requestLocationUpdates(GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(NETWORK_PROVIDER,0 , 0 , this);
    }

    private void showAlertDialog(){
        // printing tips by creating a dialog
        String mss = "GPS is not enabled. Do you want to go to setting menu?";
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Setting the GPS");
        mBuilder.setMessage(mss);

        mBuilder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        mBuilder.create().show();
    }

    private String GetLocation(Location location){
        Date date = new Date(location.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String textofDate = sdf.format(date);
        Log.i("Debug/Info: ", location.toString());

        //if you draw a circle centered at this location's latitude and longitude, and with a radius equal to the accuracy,
        // then there is a 68% probability that the true location is inside the circle.
        String result = "Date/Time: "+ textofDate +"\nProvider: "+location.getProvider() + "\nAccuracy: "+ location.getAccuracy() +
                "\nLatitude: "+ location.getLatitude() +"\nLongtitude: " +location.getLongitude()+"\nAltitude: "+ location.getAltitude()
                +"\nspeed: "+ location.getSpeed();
        return result;
    }

}
