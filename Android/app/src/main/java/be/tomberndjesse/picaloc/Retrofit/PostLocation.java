package be.tomberndjesse.picaloc.Retrofit;

import android.location.Location;

/**
 * Created by jesse on 17/09/2016.
 */
public class PostLocation {
    private double latitude;
    private double longitude;

    public PostLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PostLocation(Location location){
        if(location != null) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        }
        else{
            this.latitude = 10;
            this.longitude = 10;
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
