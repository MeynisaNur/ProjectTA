package bismillah.lenovow8.googlemapsproject;

/**
 * Created by LENOVO W8 on 26/08/2017.
 */
/*
class Type1{

    Random randomGenerator = new Random();
    private List<Marker> markers = new ArrayList<>();
    private List<Marker> mark = new ArrayList<Marker>();

    public LatLng PieceBoid(GoogleMap mMap){

        int radius = 2747;
        LatLng point = new LatLng(-7.2930277, 112.7995313);
        List<LatLng> randomPoints = new ArrayList<>();
        List<Float> randomDistances = new ArrayList<>();
        Location myLocation = new Location("");
        myLocation.setLatitude(point.latitude);
        myLocation.setLongitude(point.longitude);

        //This is to generate 10 random points
        for(int i = 0; i<10; i++) {
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


        }
        //Get nearest point to the center
        int indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances));
        return randomPoints.get(indexOfNearestPointToCentre);

    }

    public void animateBoid(Marker marker, Marker markers){

        List<Double> list = new ArrayList<Double>();
        list.add(10.0);
        list.add(2.5);
        list.add(1.25);
        double m = getRandomList(list);
        LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Spherical();
        MarkerAnimation.animateMarkerToICS
                (marker, new LatLng(markers.getPosition().latitude, markers.getPosition().longitude), latLngInterpolator, (long) m);

        for (Marker markerBoids:mark){

            for (Marker markerShelter:markers){
                if (SphericalUtil.computeDistanceBetween(markerBoids.getPosition(), markerShelter.getPosition())<250){

                }
            }}


    }

    public Double getRandomList(List<Double> list){
        int index = randomGenerator.nextInt(list.size());
        //System.out.println("\nIndex : "+index);
        //Toast.makeText(mainActivity, index+"",Toast.LENGTH_LONG).show();
        return list.get(index);
    }
}
*/
