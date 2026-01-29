package com.mecaroid.interdate.Calulators;

public class DistanceCalculator {

    public static double ofLatLongInKm(double lat1,double long1,double lat2, double long2){
        final int Radius = 6371;

        double lat1Rad = Math.toRadians(lat1);
        double long1Rad = Math.toRadians(long1);

        double lat2Rad = Math.toRadians(lat2);
        double long2Rad = Math.toRadians(long2);

        double latDiffer = lat2Rad - lat1Rad;
        double longDiffer = long2Rad - long1Rad;

//        Haversine formula
        double a = Math.sin(latDiffer / 2) * Math.sin(latDiffer / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(longDiffer / 2) * Math.sin(longDiffer / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        // Distance in kilometers

        return Radius * c;
    }
}
