package bismillah.lenovow8.googlemapsproject;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by LENOVO W8 on 11/08/2017.
 */

public class Disasters {

    private Activity mainActivity;
    Circle lastCircle;
    private List<Marker> mark = new ArrayList<>();
    Handler handler = new Handler();
    private static Disasters instance = null;
    Marker mLastMarker;
    Boolean m = true;


    private Disasters(Activity activity) {
        this.mainActivity = activity;
    } //End Disasters()


    static public Disasters getInstance(Activity activity) {

        if (instance == null) {
            instance = new Disasters(activity);
            return instance;
        }
        else {
            return instance;
        }

    } //End getInstance()


    protected void randomDisasters(GoogleMap myMap, LatLng myLocation) {

        if (lastCircle != null) {
            lastCircle.remove();
            //myMap.clear();
        }

        LatLng latLng = new LatLng(-7.2930277, 112.7995313);
        CircleOptions circle = new CircleOptions();
        circle.center(latLng).strokeColor(Color.RED).strokeWidth(1).fillColor(Color.argb(034, 250, 0, 0)).radius(2747);
        lastCircle = myMap.addCircle(circle);

        if (m == true) {
            spawnDisasters(latLng, 2747, myMap, myLocation);
        }


    } //End randomDisasters()


    public void spawnDisasters(final LatLng point, final int radius, final GoogleMap myMap, final LatLng myLoc){

        m = false;
        final int timeMIN = 5000;
        final int timeMAX = 25000;
        Random rdm = new Random(System.currentTimeMillis());

        Random rand = new Random();
        final int n = rand.nextInt(3);
        int i = 0;

        for (i = 0; i<4; i++){
            int m = rdm.nextInt(timeMAX - timeMIN + 1) + timeMIN;
        handler.postDelayed(
        new Runnable()
        {
            public void run() {

                List<LatLng> randomPoints = new ArrayList<>();
                List<Float> randomDistances = new ArrayList<>();
                Location myLocation = new Location("");
                myLocation.setLatitude(point.latitude);
                myLocation.setLongitude(point.longitude);


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

                    Marker marker = myMap.addMarker(new MarkerOptions().position(randomLatLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.boid_red)));
                    mark.add(marker);

                CircleOptions circle = new CircleOptions();
                int rad = n*100;
                circle.center(randomLatLng).strokeColor(Color.GREEN).strokeWidth(1)
                        .fillColor(Color.argb(034, 0, 250, 0)).radius(rad);
                myMap.addCircle(circle);

                float[] results = new float[1];
                Location.distanceBetween(
                        myLoc.latitude,myLoc.longitude,
                        randomLatLng.latitude, randomLatLng.longitude, results);

                    Toast.makeText(mainActivity, "WARNING... Disaster at " + randomLatLng + " "
                            + "\nRadius : "+rad +"\n"+ String.valueOf(results[0]+" m from your place"),
                            Toast.LENGTH_LONG).show();

                    try{
                       // Random rdm = new Random();
                       // int m = rdm.nextInt(6000);
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                } //End run()

                //Random rdm = new Random();
                //int m = rdm.nextInt(6000);
                //handler.postDelayed(this, m);

            } //End Runnable()
,m);} //End Loop

    } //End spawnDisasters


} //End class


