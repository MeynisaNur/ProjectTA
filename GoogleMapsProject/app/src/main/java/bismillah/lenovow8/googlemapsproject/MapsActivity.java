package bismillah.lenovow8.googlemapsproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    Cursor c = null;

    private List<Marker> markers = new ArrayList<>();
    private List<Marker> mark = new ArrayList<Marker>();
    private List<Marker> mMarker = new ArrayList<>();

    Circle lastCircle;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    } //End onCreate method


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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        drawShelter();
        //Boid markerAnimation = Boid.getInstance(this);
        //markerAnimation.generateVelocity();
        getRandomLocation(2747);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
            else{
                //Request Location Permission
                checkLocationPermission(); //add
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    } //End onMapReady method


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    } //End buildGoogleApiClient()


    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates
                    (mGoogleApiClient, mLocationRequest, this);
        }
    } //End onConnected()


    @Override
    public void onLocationChanged(Location location) {
        //mMap.clear();
        mLastLocation = location;
        //lastCircle.remove();
        //if (lastCircle != null){


        //}
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());



            mLocation(latLng);

            drawCircle(latLng);

            //move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(7));

            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        /*
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        */

    } //End onLocationChanged()

    public void mLocation(LatLng myLocation){
        Disasters disaster = Disasters.getInstance(this);
        disaster.randomDisasters(mMap, myLocation);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mGoogleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}


    @Override
    public void onConnectionSuspended(int i) {}


    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                                //Promp user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).create().show();

                /*
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
                        */

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    } //End checkLocationPermission()


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    } //End onRequestPermissionsResult()


    public void drawShelter(){

        DatabaseHelper myDbHelper = new DatabaseHelper(MapsActivity.this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLiteException sqle) {
            throw sqle;
        }
        Toast.makeText(MapsActivity.this, "Successfully Imported", Toast.LENGTH_SHORT).show();
        c = myDbHelper.query("Shelter_Table", null, null, null, null, null, null);

        if (c.moveToFirst()) {
                do {

                LatLng shelterLoc = new LatLng(c.getDouble(2), c.getDouble(3));

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(shelterLoc)
                        .visible(false)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.shelter_marker)));
                markers.add(marker);
                    getRandomLocation2(2747,shelterLoc, marker);
            } while (c.moveToNext());

        }




    } //End drawMarker()



    boolean a= true;
    private void drawCircle(LatLng point){
        //a= true;
        if (lastCircle != null){
            lastCircle.remove();

        }

        CircleOptions circle = new CircleOptions();
        circle.center(point).strokeColor(Color.BLUE).strokeWidth(1).fillColor(Color.argb(034, 0, 0, 250)).radius(250);
        lastCircle = mMap.addCircle(circle);
        //Circle circle = mMap.addCircle(new CircleOptions().center(point).strokeColor(Color.BLUE).strokeWidth(1).fillColor(Color.argb(034,0,0,255)).radius(500));


        final Intent intent = new Intent(this, TransitionActivity.class);


        for (Marker marker : markers){
            if (SphericalUtil.computeDistanceBetween(point, marker.getPosition())<250){
                marker.setVisible(true);
                if (SphericalUtil.computeDistanceBetween(point, marker.getPosition())<15){
                    Toast.makeText(MapsActivity.this, "You're in a Shelter", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        }
    } //End drawCircle()


    public LatLng getRandomLocation2(int radius, LatLng latLng, Marker markeer) {

        LatLng point = new LatLng(-7.2930277, 112.7995313);
        List<LatLng> randomPoints = new ArrayList<>();
        List<Float> randomDistances = new ArrayList<>();
        Location myLocation = new Location("");
        myLocation.setLatitude(point.latitude);
        myLocation.setLongitude(point.longitude);

        //This is to generate 10 random points
        for(int i = 0; i<2; i++) {
            double x0 = point.latitude;
            double y0 = point.longitude;

            Random random = new Random();

            // Convert radius from meters to degrees
            double radiusInDegrees = radius / 111000f;

            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            // Adjust the x-coordinate for the shrinking of the east-west distances
            double new_x = x / Math.cos(y0);

            double foundLatitude = new_x + x0;
            double foundLongitude = y + y0;
            LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
            randomPoints.add(randomLatLng);
            Location l1 = new Location("");
            l1.setLatitude(randomLatLng.latitude);
            l1.setLongitude(randomLatLng.longitude);
            randomDistances.add(l1.distanceTo(myLocation));

            Marker marker = mMap.addMarker(new MarkerOptions().visible(true).position(randomLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.boid_blue)));
            mark.add(marker);
            //Boid boids = Boid.getInstance(this);
            //boids.getRandomLocationTo(point, radius, marker);
            //drawShelter();
            List<Double> list = new ArrayList<Double>();
            list.add(10.0);
            list.add(2.5);
            list.add(1.25);
            double m = getRandomList(list);


            LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Spherical();
            MarkerAnimation.animateMarkerToICS
                    (marker, new LatLng(latLng.latitude, latLng.longitude), latLngInterpolator, (long) m);


            if (SphericalUtil.computeDistanceBetween(randomLatLng, markeer.getPosition())<250){
                marker.remove();

            }
            /*if(marker.getPosition() == markeer.getPosition()){
                Toast.makeText(MapsActivity.this, "Lalalala", Toast.LENGTH_LONG).show();
                marker.remove();
            }*/

            /*for (Marker marker1 : mark){
                if (SphericalUtil.computeDistanceBetween(point, marker1.getPosition())<2747){
                    marker1.setVisible(true);

                }
            }*/


        }
        //Get nearest point to the center
        int indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances));
        return randomPoints.get(indexOfNearestPointToCentre);

    }// End getRandomLocation()
    Random randomGenerator = new Random();
    public Double getRandomList(List<Double> list){
        int index = randomGenerator.nextInt(list.size());
        //System.out.println("\nIndex : "+index);
        //Toast.makeText(mainActivity, index+"",Toast.LENGTH_LONG).show();
        return list.get(index);
    } //end getRandomLocation


    public LatLng getRandomLocation(int radius) {

        LatLng point = new LatLng(-7.2930277, 112.7995313);
        List<LatLng> randomPoints = new ArrayList<>();
        List<Float> randomDistances = new ArrayList<>();
        Location myLocation = new Location("");
        myLocation.setLatitude(point.latitude);
        myLocation.setLongitude(point.longitude);

        //This is to generate 10 random points
        for(int i = 0; i<=10; i++) {
            double x0 = point.latitude;
            double y0 = point.longitude;

            Random random = new Random();

            // Convert radius from meters to degrees
            double radiusInDegrees = radius / 111000f;

            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            // Adjust the x-coordinate for the shrinking of the east-west distances
            double new_x = x / Math.cos(y0);

            double foundLatitude = new_x + x0;
            double foundLongitude = y + y0;
            LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
            randomPoints.add(randomLatLng);
            Location l1 = new Location("");
            l1.setLatitude(randomLatLng.latitude);
            l1.setLongitude(randomLatLng.longitude);
            randomDistances.add(l1.distanceTo(myLocation));

            Marker marker = mMap.addMarker(new MarkerOptions().visible(true).position(randomLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.boid_green)));
            mMarker.add(marker);
            Boid boids = Boid.getInstance(this);
            boids.getRandomLocationTo(point, radius, marker);
            //drawShelter();

        }
        //Get nearest point to the center
        int indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances));
        return randomPoints.get(indexOfNearestPointToCentre);

    }// End getRandomLocation()





} //End MapsActivity class