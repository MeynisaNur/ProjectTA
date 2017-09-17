package bismillah.lenovow8.googlemapsproject;

import android.app.Activity;
import android.location.Location;
import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by LENOVO W8 on 21/08/2017.
 */

public class Boid {

    private static Boid instance = null;
    private Activity mainActivity;
    private List<Marker> mark = new ArrayList<>();
    Handler handler = new Handler();
    Random randomGenerator = new Random();

    private Boid(Activity activity) {
    this.mainActivity = activity;
    } //End Disasters()


    static public Boid getInstance(Activity activity) {

        if (instance == null) {
            instance = new Boid(activity);
            return instance;
        }
        else {
            return instance;
        }

    } //End getInstance()


    public LatLng getRandomLocationTo(final LatLng point, final int radius, final Marker marker) {

        final Runnable r = new Runnable()
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

                List<Double> list = new ArrayList<Double>();
                list.add(10.0);
                list.add(2.5);
                list.add(1.25);
                double m = getRandomList(list);

                //ran =getRandomList(list);
                //MarkerAnimation obj = new MarkerAnimation(mainActivity);
                //Toast.makeText(mainActivity, getRandomList(list)+"",Toast.LENGTH_LONG).show();

                LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Spherical();
                MarkerAnimation.animateMarkerToICS
                        (marker, new LatLng(randomLatLng.latitude, randomLatLng.longitude), latLngInterpolator, (long) m);

                handler.postDelayed(this, (long) (m*50000));

            } //End run()

            //Random rdm = new Random();
            //int m = rdm.nextInt(6000);
            //handler.postDelayed(this, m);

        }; //End Runnable()

        handler.postDelayed(r, 1000);
        return null;
    }// End getRandomLocation()

    public void generateVelocity(){




    }

    public Double getRandomList(List<Double> list){
        int index = randomGenerator.nextInt(list.size());
        //System.out.println("\nIndex : "+index);
        //Toast.makeText(mainActivity, index+"",Toast.LENGTH_LONG).show();
        return list.get(index);
    }

}
