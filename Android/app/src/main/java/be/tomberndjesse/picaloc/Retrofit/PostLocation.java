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
        this(location.getLatitude(), location.getLongitude());
    }
}
