package com.example.mahe.myapplication;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GPSTracker gps;
    GoogleApiClient mgoogleClient;
    Double userLng, userLat;
    EditText ed;
  String str="SRG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Button b1 = (Button) findViewById(R.id.search);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocation(15);
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        //   return;
        // }
        //  mMap.setMyLocationEnabled(true);

        mgoogleClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mgoogleClient.connect();
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void getLocation(float f) {
        // Add a marker in Sydney and move the camera
        gps = new GPSTracker(MapsActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            userLat = gps.getLatitude();
            userLng = gps.getLongitude();


            // \n is for new line
            LatLng mylocation = new LatLng(userLat, userLng);
            CameraUpdate Update = CameraUpdateFactory.newLatLngZoom(mylocation, f);
            mMap.moveCamera(Update);
        }

    }

    public void getLocationByUser(Double latt, Double lng, float f) {
        LatLng mylocation = new LatLng(latt, lng);
        CameraUpdate Update = CameraUpdateFactory.newLatLngZoom(mylocation, f);
        mMap.moveCamera(Update);
    }

    public Marker marker;

    //b1.setOnClickListener(new)
    public void geoLocate(View view) throws IOException {

        ed = (EditText) findViewById(R.id.editText);
        String locationUser = ed.getText().toString();
        Geocoder locate = new Geocoder(this);
        if (locationUser.compareTo(" ")==1) {
            Toast.makeText(getApplicationContext(),"Enter the Location.",Toast.LENGTH_LONG).show();
        } else {

            List<Address> List = locate.getFromLocationName(locationUser, 1);
            Address add = List.get(0);
            String locality = add.getLocality();
            Toast.makeText(getApplicationContext(),"Geo Location "+ed.getText().toString()+" is Added.", Toast.LENGTH_LONG).show();
            double lat = add.getLatitude();
            double lng = add.getLongitude();
            userLat = lat;
            userLng = lng;
            str=locationUser;
            getLocationByUser(lat, lng, 20);
            setMarker(locality, lat, lng);
        }
    }


    private void setMarker(String locality, double lat, double lng) {
        if (marker != null) {
            marker.remove();
        }
        MarkerOptions option = new MarkerOptions()
                .title(locality)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(new LatLng(lat, lng));
        marker = mMap.addMarker(option);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Inflature = getMenuInflater();
        Inflature.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem Item) {
        switch (Item.getItemId()) {
            case R.id.mapTypeNone:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            case R.id.mapTypeHybriD:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            default:
                break;
        }
        return super.onOptionsItemSelected(Item);
    }

    LocationRequest Lreq;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Lreq = LocationRequest.create();
        Lreq.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Lreq.setInterval(30000);
        //startLocationUpdates();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mgoogleClient, Lreq, this);

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(getApplicationContext(), "Can't get Location.", Toast.LENGTH_SHORT).show();
        } else {
            LatLng llt = new LatLng(location.getLatitude(), location.getLongitude());
            float pk = (float) ((float) (180.f) / Math.PI);
            float a1 = (float) (location.getLatitude() / pk);
            float a2 = (float) (location.getLongitude() / pk);
            float a3 = (float) (userLat / pk);
            float a4 = (float) (userLng / pk);
            float t1 = (float) (Math.cos(a1) * Math.cos(a2) * Math.cos(a3) * Math.cos(a4));
            float t2 = (float) (Math.cos(a1) * Math.sin(a2) * Math.cos(a3) * Math.sin(a4));
            float t3 = (float) (Math.sin(a1) * Math.sin(a3));
            double tt = Math.acos(t1 + t2 + t3);
            double distance = 6366000 * tt;
            double error=distance*.3333333;
            distance=distance+error;
            String str1=String.valueOf(distance);
           /* float difflat = a1-a3;
            float difflang = a4-a2;
            double ans = (Math.sin(difflat/2)*Math.sin(difflat/2))
                    + (Math.cos(a1)*Math.cos(a3))+(Math.sin(difflang/2)*Math.sin(difflang/2));
            double ans1 =  (2*Math.atan2(Math.sqrt(ans),Math.sqrt(1-ans)));
            double distance = ans1;
            String str1 = String.valueOf(distance);*/



//Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();
                if(str.compareTo("SRG")!=0) {
                    if (distance <= 1000) {
                        Intent i = new Intent();
                        PendingIntent pint;
                        pint = PendingIntent.getActivity(MapsActivity.this, 0, i, 0);
                        Notification notmanger = new Notification.Builder(MapsActivity.this)
                                .setTicker("TickerTitle")
                                .setContentTitle("Geo Alarm")
                                .setContentText("You have some work here.")
                                .setSmallIcon(R.drawable.icon)
                                .setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_VIBRATE)
                                .setVibrate(new long[]{01})
                                .setContentIntent(pint).getNotification();
                        notmanger.flags = Notification.FLAG_AUTO_CANCEL;
                        //notmanger.defaults = Notification.DEFAULT_VIBRATE;

                        NotificationManager nmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        nmanager.notify(0, notmanger);
                        //Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        //vibrator.vibrate(10000);
                    }
                }

                //CameraUpdate update=CameraUpdateFactory.newLatLngZoom(llt,20);
                //mMap.animateCamera(update);
                //setMarker(llt.toString(),llt.latitude,llt.longitude);
            }
        }

    }
