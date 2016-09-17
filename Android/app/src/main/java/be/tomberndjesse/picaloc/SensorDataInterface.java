package be.tomberndjesse.picaloc;

import android.location.Location;

/**
 * Created by jesse on 17/09/2016.
 */
public interface SensorDataInterface {
    public void locationChanged(Location newLocation);
}
